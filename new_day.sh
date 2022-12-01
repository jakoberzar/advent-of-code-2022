#!/usr/bin/env bash
# Strict mode
set -euo pipefail
IFS=$'\n\t'

# Variables
DAY_NAME=$1
INPUT_DAY_FOLDER="inputs/day-$DAY_NAME"
KOTLIN_FOLDER="kotlin"

# Make input files
mkdir "$INPUT_DAY_FOLDER"
touch "$INPUT_DAY_FOLDER/simple.txt"
touch "$INPUT_DAY_FOLDER/full.txt"
echo "Input files created"

# Make Kotlin files
cp "$KOTLIN_FOLDER/Day01.kt" "$KOTLIN_FOLDER/Day$DAY_NAME.py"
# cp "$KOTLIN_FOLDER/day-boilerplate.kt" "$KOTLIN_FOLDER/Day$DAY_NAME.py"
# touch "$KOTLIN_FOLDER/Day$DAY_NAME.kt"
echo "Kotlin files created"
