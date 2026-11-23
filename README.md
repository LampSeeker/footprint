# Footprint

Firebase 이메일 계정으로 사진 게시물을 올리고, ARCore 평면에 표식을 배치하는 Android 앱입니다.

## 빌드

- JDK 17과 Android SDK Platform 33, Build Tools 32.0.0이 필요합니다.
- Windows에서 `gradlew.bat assembleDebug testDebugUnitTest`를 실행합니다.
- 디버그 APK는 `app/build/outputs/apk/debug/app-debug.apk`에 생성됩니다.

## Firebase

`app/google-services.json`은 기존 Firebase 프로젝트의 설정입니다. 해당 프로젝트를 사용할 수 없다면 `com.gachon.footprint` Android 앱을 Firebase에 등록하고 설정 파일을 교체하세요. Authentication에서 이메일/비밀번호 로그인을 켜고 Firestore와 Storage를 준비해야 합니다. 두 서비스의 보안 규칙에서 로그인한 사용자의 게시물 조회 및 업로드를 허용해야 합니다.

게시물은 Firestore의 `images` 컬렉션과 Storage의 `images/<uid>/` 경로를 사용합니다. 피드는 최신 50개를 표시합니다. AR 화면은 지원 기기와 Google Play Services for AR이 필요하며, 표식은 현재 기기 세션에만 표시됩니다.
