
SRC_DIR=src/main/java
BUILD_DIR=build

MAIN_CLASS=main.java.Main

# Compile step
compile:
	@echo "Compiling files..."
	@mkdir -p $(BUILD_DIR)
	@javac -d $(BUILD_DIR) $(SRC_DIR)/*.java

# Run step
run: compile
	@echo "Running the program..."
	@java -cp $(BUILD_DIR) $(MAIN_CLASS)

# Clean step
clean:
	@echo "Cleaning up..."
	@rm -rf $(BUILD_DIR)
