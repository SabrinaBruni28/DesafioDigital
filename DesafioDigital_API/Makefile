run:
	./gradlew lwjgl3:run --stacktrace

clean:
	./gradlew clean

jar:
	./gradlew lwjgl3:dist

web:
	./gradlew html:dist

webRun:
	cd html/build/dist/ && python3 -m http.server

chaveAndroid:
	keytool -genkey -v -keystore my-release-key.keystore -alias myalias -keyalg RSA -keysize 2048 -validity 10000

assinarAndroid:
	jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 \
	-keystore my-release-key.keystore \
	android/build/outputs/apk/release/android-release-unsigned.apk myalias \
	&& \
	zipalign -v 4 \
	android/build/outputs/apk/release/android-release-unsigned.apk \
	android/build/outputs/apk/release/android-release.apk

androidDebug:
	./gradlew android:assembleDebug

androidRelease:
	./gradlew android:assembleRelease

ios:
	./gradlew ios:createIPA
