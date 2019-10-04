Releasing
========

 1. Bump the `libraryVersion` property in `release-bintray.gradle` based on Major.Minor.Patch naming scheme
 2. Check `bintrayRepo` at `release-bintray.gradle` property set to `maven`
 3. Check `bintray.user` and `bintray.apikey` at `local.properties`.
 4. Update `CHANGELOG.md` for the impending release.
 5. Update `README.md` with the new version.
 6. `git commit -am "Prepare for release X.Y.Z."` (where X.Y.Z is the version you set in step 1)
 7. `git tag -a X.Y.X -m "Version X.Y.Z"` (where X.Y.Z is the new version)
 8. `./gradlew clean build bintrayUpload`
 9. `git push && git push --tags`