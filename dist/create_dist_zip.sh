#!/usr/bin/env bash
#
# ------------------------------------------------------------------------------
# This script generates the distribution zip file.
#
# Usage:
# ./create_dist_zip.sh
#
# ------------------------------------------------------------------------------

SCRIPT_DIR=$(cd `dirname $0` && pwd)

#
# Print error message and exit
#
die() {
  echo "$@" >&2

  # Remove checkout directory if it is defined and if it exists
  if [ -n "${TMP_DIR}" ] && [ -d "$TMP_DIR" ]; then
    rm -rf "$TMP_DIR"
  fi

  exit 1
}

# ------------------------------------------------------------------ Envs & Args

TMP_DIR="$SCRIPT_DIR/create_dist_zip_$$_$USER"

VERSION="1.2-SNAPSHOT"
JAR_FILENAME="jtimesched-$VERSION-jar-with-dependencies.jar"

# ------------------------------------------------------------------------- Main

pushd . > /dev/null 2>&1
cd "$SCRIPT_DIR"

  echo "Generate the distribution"
  rm -rf "$TMP_DIR"; mkdir -p "$TMP_DIR" || die "Failed to create temporary directory $TMP_DIR!"

  # Metadata
  cp -v "$SCRIPT_DIR/../README.md" "$SCRIPT_DIR/../ChangeLog.txt" "$SCRIPT_DIR/../LICENSE.txt" "$TMP_DIR" || die "Failed to copy metadata files!"

  # Execs
  cp -v "$SCRIPT_DIR/../launcher/jTimeSched.exe" "$SCRIPT_DIR/../launcher/jTimeSched.sh" "$TMP_DIR" || die "Failed to copy exec files!"
  chmod a+x "$TMP_DIR/jTimeSched.sh" || die "Failed to make $TMP_DIR/jTimeSched.sh executable!"

  cp -v "$SCRIPT_DIR/../target/$JAR_FILENAME" "$TMP_DIR/$JAR_FILENAME" || die "Failed to copy jar file!"
  chmod a+x "$TMP_DIR/$JAR_FILENAME" || die "Failed to make $TMP_DIR/$JAR_FILENAME executable!"

  # Imgs
  mkdir "$TMP_DIR/appicon" || die "Failed to create 'appicon' dir!"
  cp -v $SCRIPT_DIR/../src/main/resources/data/img/appicon/jTimeSched_on_*.png "$TMP_DIR/appicon" || die "Failed to copy appicon files!"
  cp -v "$SCRIPT_DIR/../launcher/jTimeSched.ico" "$TMP_DIR/appicon" || die "Failed to copy ico file!"

  # Create zip file
  pushd . > /dev/null 2>&1
  cd "$TMP_DIR"
    zip -r "$SCRIPT_DIR/jTimeSched-$VERSION.zip" * || die "Failed to create the distribution zip file!"
  popd > /dev/null 2>&1

popd > /dev/null 2>&1

# Clean up
rm -rf "$TMP_DIR"

echo "DONE!"
exit 0
