LOCAL_MAVEN_DIR := $(HOME)/.local/maven
LOCAL_MAVEN_VERSION := 3.9.6

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
		curl -fLO "https://dlcdn.apache.org/maven/maven-3/$(LOCAL_MAVEN_VERSION)/binaries/apache-maven-$(LOCAL_MAVEN_VERSION)-bin.tar.gz"; \
		tar -xzf apache-maven-$(LOCAL_MAVEN_VERSION)-bin.tar.gz; \
		rm -rf "$(LOCAL_MAVEN_DIR)"; \
		mv apache-maven-$(LOCAL_MAVEN_VERSION) "$(LOCAL_MAVEN_DIR)"; \
		rm -rf $$tmp; \
		echo "Maven installed locally at $(LOCAL_MAVEN_DIR)"; \
	fi
