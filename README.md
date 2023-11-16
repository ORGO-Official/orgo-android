# 오르GO
초보자도 쉽게 등산의 즐거움을 느끼고, 성취감을 얻을 수 있는 등산 기록 서비스
## Tech Stack & Libraries
- Kotlin
- Kotlin Corountines, Flow
- Hilt
- Jetpack Compose, Compose Material 3
- Navigation Compose
- Androidx Splashscreen
- Retrofit2 & OkHttp3
- Gson
- DataStore
- [Protobuf](https://github.com/google/protobuf-gradle-plugin)
- [Coil](https://github.com/coil-kt/coil)
- [NaverOauth](https://github.com/naver/naveridlogin-sdk-android)
- [KakaoSdk](https://developers.kakao.com/docs/latest/ko/kakaologin/android)
- [TedPermission](https://github.com/ParkSangGwon/TedPermission)
- [Timber](https://github.com/JakeWharton/timber)

## 주요 기능
### 완등 인증
지도를 통해 산을 선택하고 정상 주변에서 완등 인증을 할 수 있습니다.

### 산 상세정보
특정 산에 대한 정보와 주변 맛집에 대한 정보를 제공합니다.

### 마이페이지
완등한 산에 대한 기록을 제공합니다.

### 뱃지 컬렉션
산을 완등하고 얻은 뱃지, 미획득 뱃지를 확인할 수 있습니다.

## 부가 기능
로그인, 검색, 설정
### 로그인
네이버, 카카오 소셜 로그인을 제공하며, 다음에 로그인하기 버튼으로 건너뛸 수도 있습니다.
한 번 로그인하면 다음부터는 자동으로 로그인 됩니다.

### 검색
산에 대한 검색 기능을 제공합니다.

### 설정
기본적으로 이용약관, 개인정보처리방침을 WebView형태로 확인할 수 있도록하며,
로그인 했을 경우 로그아웃, 회원탈퇴 기능을 제공합니다.

## Architecture
- MVVM 아키텍처
<p align="center">
<img src="/previews/architecture.png"/>
</p>

### Module
![image](https://github.com/ORGO-Official/orgo-android/assets/38021810/90cc83b3-6083-4a1f-a004-6ea1d6683312)
Multi-module 구조로 설계하였습니다.

각 feature마다 layer를 나누지 않았고 domain,data layer는 core:domain, core:data에 전부 구현하였습니다.


