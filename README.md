## 프로젝트 이름 : 산책갈개🐕‍🦺 ( JPA Team Project )

<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/eaaf26ee-8288-4b51-9f14-46f4db4b8664"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/eaaf26ee-8288-4b51-9f14-46f4db4b8664?type=w580" width="500">
</a>

### 🐕프로젝트 주제 

애완견 산책을 **함께**  할 수 있는 커뮤니티
1. 쉽고 가벼운 **산책 메이트** 매칭
2. 자유로운 소통을 위한 **커뮤니티** 공간
3. 애완견이 좋아할 **쇼핑** 공간
4. 애완견의 건강를 위한 **동물병원** 찾기
5. 활동을 체크 할 수 있는 **마이 페이지** 
6. 사이트 관리를 위한 **관리자 페이지** 

## 📖목차
  - [팀 구성](#팀-구성)
  - [페이지 흐름](#페이지-흐름도)
  - [ERD구성](#erd구성)
  - [Skill](#skill)
  - [담당 작업](#담당-작업)
    - [자유게시판 WIKI로 이동](#자유게시판--wiki로-이동)
    - [공지사항 WIKI로 이동](#공지사항-wiki로-이동)
    - [쇼핑리스트 WIKI로 이동](#쇼핑리스트-wiki로-이동)
    - [쇼핑(상세보기) WIKI로 이동](#-쇼핑상세보기-wiki로-이동)
    - [장바구니 WIKI로 이동](#장바구니-wiki로-이동)
    - [장바구니(결제) WIKI로 이동](#장바구니결제-wiki로-이동)
    - [장바구니(결제진행) WIKI로 이동](#장바구니결제진행-wiki로-이동)
    - [카카오(결제) WIKI로 이동](#카카오결제-wiki로-이동)
    - [리뷰작성(결제완료후) WIKI로 이동](#리뷰작성결제완료후-wiki로-이동)

## 🔗팀 구성
**Producer Git-hut Page 노의진**

|팀장|노의진|           
|:--:|:--:|
|팀원|복영헌| 
|팀원|임형준| 

## 🧷페이지 흐름도
<details open>
<summary>페이지 흐름도</summary>
<img src='https://github.com/NohEuijin/JPA-DW/assets/141835418/2840096d-a01d-4065-8635-74585a91c089' border='0'>
<img src='https://github.com/NohEuijin/JPA-DW/assets/141835418/a0b9f8a9-d5e3-48a8-9afa-8a12c4837ee0' border='0'>
</details>

## 🧷ERD구성

<details open>
<summary>ERD</summary>
  
<a href='https://github.com/NohEuijin/JPA-DW/assets/141835418/c46f7e89-697f-4b4b-bda2-7ec5481f3248' target='_blank'>
<img src='https://github.com/NohEuijin/JPA-DW/assets/141835418/c46f7e89-697f-4b4b-bda2-7ec5481f3248' border='0'>
</a>

</details>

 ## 🗡Skill
- **JPA**
### DB
- **Oracle**

## 🧒담당 작업

### 🐶자유게시판 <a href="https://github.com/NohEuijin/JPA-DW/wiki/%F0%9F%90%B6%EC%9E%90%EC%9C%A0%EA%B2%8C%EC%8B%9C%ED%8C%90"> WIKI로 이동</a>
- **기본 CRUD** : 글 쓰기, 글 상세 보기, 글 수정, 글 삭제
- **댓글 CRUD** : 댓글 쓰기, 댓글 보기, 댓글 수정, 댓글 삭제
- **파일 처리** : 이미지 파일 넣기, 이미지 보기, 이미지 수정 시 삭제 후 수정
- **검색 기능** : 게시글 검색, 최신순, 인기순, 댓글순
- **추가 기능** : 조회수 중복 방지, 페이징 처리
<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/f5495e28-e4fb-4f9e-a362-e6bc4033e61d"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/f5495e28-e4fb-4f9e-a362-e6bc4033e61d?type=w580" width="500">
</a>

### 🐶공지사항<a href="https://github.com/NohEuijin/JPA-DW/wiki/%F0%9F%90%B6%EA%B3%B5%EC%A7%80%EC%82%AC%ED%95%AD"> WIKI로 이동</a>
- **검색 기능**
- **공지사항 보기**
- **추가 기능** : 조회수 중복 방지, 페이징 처리
<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/581b5f80-7a80-4f19-8535-0fdbdcfaffe6"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/581b5f80-7a80-4f19-8535-0fdbdcfaffe6?type=w580" width="500">
</a>

### 🐶쇼핑(리스트)<a href="https://github.com/NohEuijin/JPA-DW/wiki/%F0%9F%90%B6%EC%87%BC%ED%95%91(%EB%A6%AC%EC%8A%A4%ED%8A%B8)"> WIKI로 이동</a>
- **검색 기능** : 물품 검색, 인기순, 최신순, 낮은 가격순, 높은 가격 순
- **리스트 검색 기능** : 전체, 간식, 영양제, 위생용품, 이동장, 장난감, 산책용품
- **추가 기능** : 페이징 처리, 장바구니 담기, 장바구니 이동, 리뷰 평균 별점 보기, 리뷰 갯수 보기
<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/8d500331-d885-499d-8693-80d5efd10f0f"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/8d500331-d885-499d-8693-80d5efd10f0f?type=w580" width="500">
</a>

### 🐶 쇼핑(상세보기)<a href="https://github.com/NohEuijin/JPA-DW/wiki/%F0%9F%90%B6%EC%87%BC%ED%95%91(%EC%83%81%EC%84%B8%EB%B3%B4%EA%B8%B0)"> WIKI로 이동</a>
- **리뷰 보기**
- **문의 CRUD** : 문의 쓰기, 문의 보기, 글 수정, 글 삭제
- **추가 기능** : 장바구니 담기, 장바구니 이동, 리뷰 평균 별점 보기, 리뷰 갯수 보기, 상품 이미지 파일 처리
<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/9d2315e5-8a67-4d31-a7c1-b7f21dbdadf8"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/9d2315e5-8a67-4d31-a7c1-b7f21dbdadf8?type=w580" width="500">
</a>

### 🐶장바구니<a href="https://github.com/NohEuijin/JPA-DW/wiki/%F0%9F%90%B6%EC%9E%A5%EB%B0%94%EA%B5%AC%EB%8B%88"> WIKI로 이동</a>
- **기능** : 물품 장바구니 추가 및 장바구니 추가 후 이동
- **세션 기능** : 로그아웃 후 로그인시, 해당 회원 장바구니 물품 유지
- **물품 개수 추가 및 삭제**

<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/009d9089-1679-492d-a493-0921e7cab25b"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/009d9089-1679-492d-a493-0921e7cab25b?type=w580" width="500">
</a>

### 🐶장바구니 확인(주문서 작성)<a href="https://github.com/NohEuijin/JPA-DW/wiki/%F0%9F%90%B6%EC%9E%A5%EB%B0%94%EA%B5%AC%EB%8B%88-%ED%99%95%EC%9D%B8(%EC%A3%BC%EB%AC%B8%EC%84%9C-%EC%9E%91%EC%84%B1)"> WIKI로 이동</a>

<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/cfb6e113-32cd-44cb-a8aa-3ddc4c0ec3ed"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/cfb6e113-32cd-44cb-a8aa-3ddc4c0ec3ed?type=w580" width="500">
</a>

### 🐶장바구니(결제진행)<a href="https://github.com/NohEuijin/JPA-DW/wiki/%F0%9F%90%B6%EC%9E%A5%EB%B0%94%EA%B5%AC%EB%8B%88(%EA%B2%B0%EC%A0%9C%EC%A7%84%ED%96%89)"> WIKI로 이동</a>
- 정보를 받아 카카오결제

<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/1eb9eac3-cd8c-452f-bd55-4a046caa0a57"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/1eb9eac3-cd8c-452f-bd55-4a046caa0a57?type=w580" width="500">
</a>

### 🐶카카오(결제)<a href="https://github.com/NohEuijin/JPA-DW/wiki/%F0%9F%90%B6%EC%B9%B4%EC%B9%B4%EC%98%A4(%EA%B2%B0%EC%A0%9C)"> WIKI로 이동</a>
- 완료 후 DB 저장 

<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/78cab804-7bbd-4570-9548-c60459c8a398"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/78cab804-7bbd-4570-9548-c60459c8a398?type=w580" width="500">
</a>

### 🐶리뷰작성(결제완료후)<a href="https://github.com/NohEuijin/JPA-DW/wiki/%F0%9F%90%B6%EB%A6%AC%EB%B7%B0%EC%9E%91%EC%84%B1(%EA%B2%B0%EC%A0%9C-%EC%99%84%EB%A3%8C-%ED%9B%84)"> WIKI로 이동</a>
- 결제 후 리뷰 작성 시, 해당 상품 리뷰 확인 가능

<a href="https://github.com/NohEuijin/JPA-DW/assets/141835418/be3134f5-19d9-4225-8eac-418b4fb47469"> 
 <img src="https://github.com/NohEuijin/JPA-DW/assets/141835418/be3134f5-19d9-4225-8eac-418b4fb47469?type=w580" width="500">
</a>
