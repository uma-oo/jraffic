#!/usr/bin/env bash
set -euo pipefail

# User-tweakable:
MAVEN_VERSION="${1:-3.9.11}"   # default version; you may pass a version as first arg

# Derived:
INSTALL_BASE="$HOME/.local"
INSTALL_DIR="$INSTALL_BASE/apache-maven-$MAVEN_VERSION"
TMPDIR="$(mktemp -d)"
DOWNLOAD_BASE="https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries"
TARBALL="apache-maven-$MAVEN_VERSION-bin.tar.gz"
TARBALL_URL="$DOWNLOAD_BASE/$TARBALL"
SHA_URL="$TARBALL_URL.sha512"

echo "==> Installing Apache Maven $MAVEN_VERSION locally to: $INSTALL_DIR"
echo "==> Temp dir: $TMPDIR"

# Ensure target dir exists
mkdir -p "$INSTALL_BASE"

# Download tarball (try curl then wget)
cd "$TMPDIR"
if command -v curl >/dev/null 2>&1; then
  echo "==> Downloading $TARBALL with curl..."
  curl -fLO "$TARBALL_URL"
  curl -fLO "$SHA_URL" || echo "==> No sha512 file available or failed to fetch sha512 (continuing)..."
elif command -v wget >/dev/null 2>&1; then
  echo "==> Downloading $TARBALL with wget..."
  wget -q "$TARBALL_URL"
  wget -q "$SHA_URL" || echo "==> No sha512 file available or failed to fetch sha512 (continuing)..."
else
  echo "ERROR: neither curl nor wget found. Install one or download manually." >&2
  exit 1
fi

# Verify sha512 if possible
if [ -f "$TARBALL.sha512" ] && command -v sha512sum >/dev/null 2>&1; then
  echo "==> Verifying sha512 checksum..."
  # Some .sha512 files contain only the checksum or checksum + filename. Use sha512sum -c if formatted, otherwise compute and compare.
  if sha512sum -c "$TARBALL.sha512" >/dev/null 2>&1; then
    echo "==> Checksum OK (sha512sum -c)."
  else
    # fallback: compare computed checksum to first token in file
    expected="$(awk '{print $1}' "$TARBALL.sha512")"
    actual="$(sha512sum "$TARBALL" | awk '{print $1}')"
    if [ "$expected" = "$actual" ]; then
      echo "==> Checksum OK (manual compare)."
    else
      echo "ERROR: sha512 mismatch!" >&2
      echo "Expected: $expected" >&2
      echo "Actual:   $actual" >&2
      exit 1
    fi
  fi
else
  echo "==> Skipping sha512 verification (sha512 file not present or sha512sum missing)."
fi

# Extract
echo "==> Extracting..."
tar -xzf "$TARBALL"
rm -rf "$INSTALL_DIR"
mv "apache-maven-$MAVEN_VERSION" "$INSTALL_DIR"

echo "==> Maven installed at $INSTALL_DIR"

# Determine which shell rc file to update
SHELL_NAME="$(basename "${SHELL:-/bin/bash}")"
RC_FILES=()
# prefer ~/.bashrc for bash, ~/.zshrc for zsh, fallback to ~/.profile
if [ "$SHELL_NAME" = "zsh" ] && [ -f "$HOME/.zshrc" ]; then
  RC_FILES+=("$HOME/.zshrc")
elif [ "$SHELL_NAME" = "bash" ] && [ -f "$HOME/.bashrc" ]; then
  RC_FILES+=("$HOME/.bashrc")
fi
# always add ~/.profile as safe fallback if not already added
if [ -f "$HOME/.profile" ]; then
  RC_FILES+=("$HOME/.profile")
else
  RC_FILES+=("$HOME/.bashrc")
fi

# Make idempotent env lines
ENV_SNIPPET="
# Apache Maven local install ($MAVEN_VERSION) - added by install-maven-local.sh
export M2_HOME=\"$INSTALL_DIR\"
export MAVEN_HOME=\"\$M2_HOME\"
export PATH=\"\$M2_HOME/bin:\$PATH\"
"

echo "==> Adding environment vars to: ${RC_FILES[*]}"
for rc in "${RC_FILES[@]}"; do
  # add only if not already present
  if grep -Fq "Apache Maven local install ($MAVEN_VERSION) - added by install-maven-local.sh" "$rc" 2>/dev/null; then
    echo "   (already updated) $rc"
  else
    echo "$ENV_SNIPPET" >> "$rc"
    echo "   (appended) $rc"
  fi
done

# Clean up
cd ~
rm -rf "$TMPDIR"

echo
echo "==> Done. To start using mvn now either:"
echo "    1) run: source ${RC_FILES[0]}"
echo "    2) or open a new terminal session"
echo
# Try to run mvn -version using the newly installed binary directly (without requiring source)
if [ -x "$INSTALL_DIR/bin/mvn" ]; then
  echo "==> Running $INSTALL_DIR/bin/mvn -version to verify:"
  "$INSTALL_DIR/bin/mvn" -version || true
else
  echo "WARNING: $INSTALL_DIR/bin/mvn is not executable or missing."
fi

echo
echo "If you want to uninstall later, remove the directory:"
echo "    rm -rf $INSTALL_DIR"
echo "and remove the added lines from your rc file(s): ${RC_FILES[*]}"
