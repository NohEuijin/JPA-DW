## 프로젝트 이름 : 산책갈개🦮 ( JPA Team Project )

### 📚프로젝트 주제 

혼자가 당연시 되는 애완견 산책을 혼자가 아닌 **함께**라는 의미를 가지고 활동을 할 수 있는 확실한 **목적성**을 가진 사이트
1. 무료 **상담사 등록** 기능 및 유료 상담사 제휴 활동
2. 일반회원의 상담을 유연하게 해 줄 **상담사 매칭** 
3. 회원 본인만의 **일기장**으로 사용 가능한 게시판과 마이페이지에서의 관리
4. 나의 집 인근에 있는 **상담소**에 대한 매핑 
5. 마음에 안정을 줄 수 있는 **음악감상** 
6. 나의 상태를 체크 해줄 **우울증 검사** 

## 📖목차
  - [팀 구성](#팀-구성)
  - [ERD구성](#erd구성)
  - [Skill](#skill)
  - [담당 작업](#담당-작업)
    - [메인페이지 WIKI로 이동](#메인페이지--wiki로-이동)
    - [상담사등록 WIKI로 이동](#상담사-등록-wiki로-이동)
    - [상담사 프로필(목록) WIKI로 이동](#상담사-프로필목록-wiki로-이동)
    - [상담사 프로필(상세보기) WIKI로 이동](#상담사-프로필목록-wiki로-이동)
    - [상담 예약 회원 관리 WIKI로 이동](#상담예약-회원관리-wiki로-이동)
    - [제휴등록 WIKI로 이동](#제휴등록-wiki로-이동)

## 🔗팀 구성
**Producer Git-hut Page 노의진**

|팀장|노의진|           
|:--:|:--:|
|팀원|복영헌| 
|팀원|임형준| 

## 💡페이지 흐름도
<details open>
<summary>페이지 흐름도</summary>
<img src='https://github.com/NohEuijin/JPA-DW/assets/141835418/2840096d-a01d-4065-8635-74585a91c089' border='0'>
<img src='https://github.com/NohEuijin/JPA-DW/assets/141835418/a0b9f8a9-d5e3-48a8-9afa-8a12c4837ee0' border='0'>
</details>

## 💡ERD구성

<details open>
<summary>ERD</summary>
  
  <a href='https://ifh.cc/v-CsWvC9' target='_blank'><img src='https://github.com/NohEuijin/JPA-DW/assets/141835418/c46f7e89-697f-4b4b-bda2-7ec5481f3248' border='0'></a>

</details>

 ## 🗡Skill
- **JPA**
### DB
- **Oracle**

### 🔖사이트 소개 <a href=""> WIKI로 이동</a>
- 로그인시 세션 유지
- 세션에 따른 헤더 메뉴 등 매핑 다름
- 세션의 레벨에 따라 000 님 , 000 상담사 님 , 000 관리자 님 으로 구분
- 세션의 레벨에 따라 드롭다운 목록이 변경
<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/eaaf26ee-8288-4b51-9f14-46f4db4b8664"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/eaaf26ee-8288-4b51-9f14-46f4db4b8664?type=w580" width="500">
</a>

## 🏷담당 작업

### 🔖자유게시판 <a href=""> WIKI로 이동</a>
- 로그인시 세션 유지
- 세션에 따른 헤더 메뉴 등 매핑 다름
- 세션의 레벨에 따라 000 님 , 000 상담사 님 , 000 관리자 님 으로 구분
- 세션의 레벨에 따라 드롭다운 목록이 변경
<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/f5495e28-e4fb-4f9e-a362-e6bc4033e61d"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/f5495e28-e4fb-4f9e-a362-e6bc4033e61d?type=w580" width="500">
</a>

### 🔖공지사항<a href=""> WIKI로 이동</a>
- 상담사는 유료상담사, 무료상담사로 구분
- 상담사 회원은 결제를 완료하면 유료 상담사로 활동이 가능
- 유료 상담사는 등록 페이지에서 비용을 입력 가능한 칸이 나타남
- 상담사의 프로필 이미지를 첨부파일로 등록이 가능

<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/581b5f80-7a80-4f19-8535-0fdbdcfaffe6"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/581b5f80-7a80-4f19-8535-0fdbdcfaffe6?type=w580" width="500">
</a>

### 🔖쇼핑(리스트)<a href=""> WIKI로 이동</a>
- 비용을 기준으로 찾아와 보여줌.

<a href=""> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/8d500331-d885-499d-8693-80d5efd10f0f?type=w580" width="500">
</a>

### 🔖 쇼핑(상세보기)<a href=""> WIKI로 이동</a>
- 상담사 목록에서 상담사 프로필 클릭시 상세보기로 이동
- 로그인한 본인의 상담사 상세보기 페이지라면 삭제가 가능
- 예약 버튼 클릭 시 예약 페이지로 이동
- 상담 후 댓글이 가능

<a href=""> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/9d2315e5-8a67-4d31-a7c1-b7f21dbdadf8?type=w580" width="500">
</a>

### 🔖장바구니<a href=""> WIKI로 이동</a>
- 상담사는 로그인시 드롭다운에 상담예약관리가 생김
- 상담 예약 관리 페이지에서는 이름, 상담날짜, 시간으로 검색이 가능(비동기)
- 해당 페이지는 10을 기준으로 페이징 처리(비동기)
- 상담을 예약한 회원의 정보를 번호와 이메일, 상담 내역을 모달창으로 확인 가능(비동기
- 상담취소 또는 상담완료 버튼으로 관리 가능

<a href=""> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/009d9089-1679-492d-a493-0921e7cab25b?type=w580" width="500">
</a>

### 🔖장바구니(결제)<a href=""> WIKI로 이동</a>
- 제휴를 등록하면 유료상담사가 됨
- 결제 버튼을 누르면 카카오페이로 결제가 진행
- 결제 진행은 비동기로 처리

<a href=""> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/cfb6e113-32cd-44cb-a8aa-3ddc4c0ec3ed?type=w580" width="500">
</a>

### 🔖장바구니(결제진행)<a href=""> WIKI로 이동</a>
- 제휴를 등록하면 유료상담사가 됨
- 결제 버튼을 누르면 카카오페이로 결제가 진행
- 결제 진행은 비동기로 처리

<a href=""> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/1eb9eac3-cd8c-452f-bd55-4a046caa0a57?type=w580" width="500">
</a>

### 🔖카카오(결제)<a href=""> WIKI로 이동</a>
- 제휴를 등록하면 유료상담사가 됨
- 결제 버튼을 누르면 카카오페이로 결제가 진행
- 결제 진행은 비동기로 처리

<a href=""> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/78cab804-7bbd-4570-9548-c60459c8a398?type=w580" width="500">
</a>

### 🔖리뷰작성(결제완료후)<a href=""> WIKI로 이동</a>
- 제휴를 등록하면 유료상담사가 됨
- 결제 버튼을 누르면 카카오페이로 결제가 진행
- 결제 진행은 비동기로 처리

<a href=""> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/be3134f5-19d9-4225-8eac-418b4fb47469?type=w580" width="500">
</a>
