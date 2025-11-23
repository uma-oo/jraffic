LOCAL_MAVEN_DIR := $(HOME)/.local/maven
LOCAL_MAVEN_VERSION := 3.9.11
MAVEN_URL := https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.zip

all: run

run: mvn
	@echo "Running JTraffic..."
	@mvn clean javafx:run

mvn:
	@if command -v mvn >/dev/null 2>&1; then \
		echo "Maven Already installed, Skipping..."; \
	else \
		echo "Maven not found. Installing locally..."; \
		mkdir -p "$(LOCAL_MAVEN_DIR)"; \
        tmp=$$(mktemp -d); \
        cd $$tmp; \
        echo "Downloading Maven $(LOCAL_MAVEN_VERSION)..."; \
        wget -q "$(MAVEN_URL)"; \
        unzip -q "apache-maven-$(LOCAL_MAVEN_VERSION)-bin.zip"; \
        rm -rf "$(LOCAL_MAVEN_DIR)"; \
        mv "apache-maven-$(LOCAL_MAVEN_VERSION)" "$(LOCAL_MAVEN_DIR)"; \
		echo 'export PATH=~/maven/bin:$PATH' >> ~/.zshrc; \
		source ~/.zshrc; \
        rm -rf $$tmp; \
        echo "Maven installed locally at $(LOCAL_MAVEN_DIR)"; \
	fi
