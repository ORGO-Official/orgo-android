# 오르GO
초보자도 쉽게 등산의 즐거움을 느끼고, 성취감을 얻을 수 있는 등산 기록 서비스입니다. 

<p align="center">
<img src="/previews/main_preview.png"/>
</p>

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
### 지도 및 검색

<p align="center">
<img src="/previews/map_preview.png"/>
</p>

- 지도 또는 검색을 통해 산을 선택하면 산 정보를 간단히 보여주는 BottomSheet가 올라옵니다.
- 완등 인증 버튼은 정상 주변에서 활성화 됩니다.
- 산에 대한 검색 기능을 제공합니다.

### 완등 인증

<p align="center">
<img src="/previews/climb_complete_preview.png"/>
</p>

- 정상 주변에서 완등 인증을 할 수 있으며 성공 시 완등 결과를 화면으로 이동합니다.
- 배경 이미지를 수정, 스크린샷을 저장할 수 있습니다. 

### 산 상세정보

<p align="center">
<img src="/previews/mountain_detail_preview.png"/>
</p>

- 특정 산에 대한 정보와 주변 맛집에 대한 정보를 제공합니다.

### 마이페이지

<p align="center">
<img src="/previews/mypage_preview.png"/>
</p>

- 완등한 산에 대한 기록을 제공합니다.

### 뱃지 컬렉션

<p align="center">
<img src="/previews/badge_preview.png"/>
</p>

- 산을 완등하고 얻은 뱃지, 미획득 뱃지를 확인할 수 있습니다.

## 부가 기능
### 로그인

<p align="center">
<img src="/previews/login_preview.png"/>
</p>

- 네이버, 카카오 소셜 로그인을 제공하며, 다음에 로그인하기 버튼으로 건너뛸 수도 있습니다.
- 한 번 로그인하면 다음부터는 자동으로 로그인 됩니다.

### 설정

<p align="center">
<img src="/previews/settings_preview.png"/>
</p>

- 기본적으로 이용약관, 개인정보처리방침을 WebView형태로 확인할 수 있도록하며,
- 로그인 했을 경우 로그아웃, 회원탈퇴 기능을 제공합니다.

## Architecture
MVVM 아키텍처, 
[안드로이드 권장 아키텍처](https://developer.android.com/topic/architecture#recommended-app-arch)
를 기반으로 하고 있습니다.
<p align="center">
<img src="/previews/architecture.png"/>
</p>

### Multi Module
각 feature마다 계층을 나누는 건 불필요하다고 판단해 core:domain, core:data에 각 feature에 필요한 domain,data 계층을 전부 구현하였습니다.
![image](https://github.com/ORGO-Official/orgo-android/assets/38021810/90cc83b3-6083-4a1f-a004-6ea1d6683312)



