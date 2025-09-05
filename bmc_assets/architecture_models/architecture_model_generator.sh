#!/bin/bash
# - set -euo pipefail Enables strict mode:
#       -e: exit immediately if any command fails.
#       -u: treat unset variables as errors.
#       -o pipefail: return failure if any command in a pipeline fails.
set -euo pipefail

# Add some ANSI colors, what's life without whimsy =)
GREEN="\033[0;32m"
YELLOW="\033[0;33m"
RED="\033[0;31m"
BLUE="\033[0;34m"
BOLD="\033[1m"
NC="\033[0m" # No Color

# Location: models/arch_model_generator.sh
# Loads config from: models/model_config.env
# Uses:
#   - models/${MODEL}/structure_file.txt
#   - models/${MODEL}/readme_templates/
# Creates folders under:
#   ../src/main/java/com/bmc/anvil/${APP_NAME}

# Making the script location agnostic
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$SCRIPT_DIR"

CONFIG_FILE="model_config.env"
if [[ ! -f "$CONFIG_FILE" ]]; then
    echo "Configuration file not found: $CONFIG_FILE"
    exit 1
fi

# Load target APP_NAME and target MODEL from config
# Expected keys in model_config.env:
#   APP_NAME=<app_name>
#   MODEL=<model_folder_name>

# Auto-exports variables assigned in the current shell
set -a
# shellcheck disable=SC1090
. "$CONFIG_FILE"
set +a

: "${APP_NAME:?Missing APP_NAME in $CONFIG_FILE}"
: "${MODEL:?Missing MODEL in $CONFIG_FILE}"
: "${ROOT_DIR:?Missing ROOT_DIR in $CONFIG_FILE}"

ROOT_DIR="$ROOT_DIR/$APP_NAME"
STRUCTURE_FILE="$MODEL/${MODEL}_structure.txt"
TEMPLATE_PATH="$MODEL/readme_templates"

if [[ ! -d "$MODEL" ]]; then
    echo -e "${RED}Model folder not found: ${BOLD}$MODEL${NC}"
    exit 1
fi
if [[ ! -f "$STRUCTURE_FILE" ]]; then
    echo -e "${RED}Structure file not found: ${BOLD}$STRUCTURE_FILE${NC}"
    exit 1
fi
if [[ ! -d "$TEMPLATE_PATH" ]]; then
    echo -e "${RED}Template directory not found: ${BOLD}$TEMPLATE_PATH${NC}"
    exit 1
fi

echo "Using APP_NAME=$APP_NAME"
echo "Using MODEL=$MODEL"
echo "Root directory: $ROOT_DIR"
echo "Structure file: $STRUCTURE_FILE"
echo "Template path:  $TEMPLATE_PATH"
echo

# One-pass processing: for each "folder: template.md" line
# - Create the folder under ROOT_DIR
# - If template exists, copy to README.md in that folder

# - The || ensures the last line is processed even if it lacks a trailing newline.
while IFS= read -r line || [[ -n "${line:-}" ]]; do
    # Skip empty lines and comments
    [[ -z "${line// /}" ]] && continue
    [[ "$line" =~ ^[[:space:]]*# ]] && continue

    # Split the line at the first colon into path and template
    IFS=':' read -r raw_path raw_tmpl <<<"$line"

    # Trim whitespace
    path_rel="$(echo "${raw_path:-}" | xargs)"
    tmpl_name="$(echo "${raw_tmpl:-}" | xargs)"

    # Require a relative path, skip if after trimming we get empty path
    [[ -z "$path_rel" ]] && continue

    # create final target folder
    folder="$ROOT_DIR/$path_rel"
    mkdir -p "$folder"

    doc_echo="with doc: ${YELLOW}[$tmpl_name]${NC}"
    # if there is a readme file for the given path, we add it, else we do nothing
    if [[ -n "$tmpl_name" ]]; then
        tmpl_file="$TEMPLATE_PATH/$tmpl_name"
        if [[ -f "$tmpl_file" ]]; then
            cp "$tmpl_file" "$folder/$tmpl_name"
        else
            doc_echo="${YELLOW}with doc -> Template not found: $tmpl_file${NC}"
        fi
    fi
    echo -e "Created folder: ${BLUE}${BOLD}$folder${NC} -> $doc_echo"

done <"$STRUCTURE_FILE"

main_readme="${MODEL}.md"
main_readme_file="$TEMPLATE_PATH/$main_readme"

if [ -f $main_readme_file ]; then
    cp "$main_readme_file" "$ROOT_DIR/$main_readme"
    echo
    echo -e "-> Main README Template for Model found: ${YELLOW}[$main_readme]${NC} added to ${BLUE}$ROOT_DIR/${NC}"
else
    echo -e "${YELLOW}-> There is no Main README Template for Model: $MODEL${NC}"
fi

echo
echo "All folders and README.md files processed successfully."
