LOCAL_MAVEN_DIR = $(HOME)/maven
LOCAL_MAVEN_VERSION = 3.9.11
MAVEN_URL = "https://dlcdn.apache.org/maven/maven-3/3.9.11/binaries/apache-maven-3.9.11-bin.tar.gz"

all: run

run: mvn
	@echo "Running JTraffic..."
	@mvn clean javafx:run

mvn:
	@if command -v mvn >/dev/null 2>&1; then \
		echo "Maven Already installed, Skipping..."; \
	else \
    cd $(HOME); \
    echo "Maven not found. Installing locally..."; \
    echo "Downloading Maven $(LOCAL_MAVEN_VERSION)..."; \
    wget -q "$(MAVEN_URL)"; \
    tar -xzf apache-maven-$(LOCAL_MAVEN_VERSION)-bin.tar.gz; \
    mv "apache-maven-$(LOCAL_MAVEN_VERSION)" "$(LOCAL_MAVEN_DIR)"; \
    echo 'export MAVEN_HOME="$(LOCAL_MAVEN_DIR)"' >> ~/.zshrc; \
    echo 'export PATH="$$MAVEN_HOME/bin:$$PATH"' >> ~/.zshrc; \
    zsh; \
    echo "Maven installed locally at $(LOCAL_MAVEN_DIR)"; \
fi
