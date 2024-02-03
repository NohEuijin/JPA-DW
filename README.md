## 프로젝트 이름 : 산책갈개🦮 ( JPA Team Project )

### 📚프로젝트 주제 

**마음의 안정**을 필요로 하는 사람들이 이용하고 **버팀목**이 될 수 있는 사이트
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
|부팀장|이지윤| 
|팀원|이동현| 
|팀원|유대훈| 

## 💡ERD구성

<details open>
<summary>ERD</summary>
  
  <a href='https://ifh.cc/v-CsWvC9' target='_blank'><img src='https://github.com/NohEuijin/JPA-DW/assets/141835418/c46f7e89-697f-4b4b-bda2-7ec5481f3248' border='0'></a>

</details>

 ## 🗡Skill
- **Java**
- **Spring Boot**
### DB
- **Oracle**

## 🏷담당 작업

### 🔖메인페이지 <a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EB%A9%94%EC%9D%B8%ED%8E%98%EC%9D%B4%EC%A7%80"> WIKI로 이동</a>
- 로그인시 세션 유지
- 세션에 따른 헤더 메뉴 등 매핑 다름
- 세션의 레벨에 따라 000 님 , 000 상담사 님 , 000 관리자 님 으로 구분
- 세션의 레벨에 따라 드롭다운 목록이 변경
<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EB%A9%94%EC%9D%B8%ED%8E%98%EC%9D%B4%EC%A7%80"> 
 <img src="https://github.com/2teamSpring/RedMedicine/assets/141835418/59c8569b-cd25-48fa-aa52-ad59d1f155aa?type=w580" width="500">
</a>

### 🔖상담사 등록<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EC%83%81%EB%8B%B4%EC%82%AC%EB%93%B1%EB%A1%9D"> WIKI로 이동</a>
- 상담사는 유료상담사, 무료상담사로 구분
- 상담사 회원은 결제를 완료하면 유료 상담사로 활동이 가능
- 유료 상담사는 등록 페이지에서 비용을 입력 가능한 칸이 나타남
- 상담사의 프로필 이미지를 첨부파일로 등록이 가능

<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EC%83%81%EB%8B%B4%EC%82%AC%EB%93%B1%EB%A1%9D"> 
<img src="https://github.com/NohEuijin/RedMedicine2/assets/141835418/bca6b628-fc70-43a7-8805-6a2ff7e3d9af?type=w580" width="500">
<img src="https://github.com/NohEuijin/RedMedicine2/assets/141835418/88e6f01e-e3aa-4c21-bcc9-d6220f75e911" width="500">
</a>

### 🔖상담사 프로필(목록)<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EC%83%81%EB%8B%B4%EC%82%AC%ED%94%84%EB%A1%9C%ED%95%84(%EB%AA%A9%EB%A1%9D%EB%B3%B4%EA%B8%B0)#%EA%B8%B0%EB%8A%A5%EC%86%8C%EA%B0%9C"> WIKI로 이동</a>
- 비용을 기준으로 찾아와 보여줌.

<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EC%83%81%EB%8B%B4%EC%82%AC%ED%94%84%EB%A1%9C%ED%95%84(%EB%AA%A9%EB%A1%9D%EB%B3%B4%EA%B8%B0)#%EA%B8%B0%EB%8A%A5%EC%86%8C%EA%B0%9C"> 
 <img src="https://github.com/2teamSpring/RedMedicine/assets/141835418/8da2cb24-0160-4239-b2b9-2022da9132ad?type=w580" width="500">
</a>

### 🔖상담사 프로필(상세보기)<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EC%83%81%EB%8B%B4%EC%82%AC%ED%94%84%EB%A1%9C%ED%95%84(%EC%83%81%EC%84%B8%EB%B3%B4%EA%B8%B0)"> WIKI로 이동</a>
- 상담사 목록에서 상담사 프로필 클릭시 상세보기로 이동
- 로그인한 본인의 상담사 상세보기 페이지라면 삭제가 가능
- 예약 버튼 클릭 시 예약 페이지로 이동
- 상담 후 댓글이 가능

<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EC%83%81%EB%8B%B4%EC%82%AC%ED%94%84%EB%A1%9C%ED%95%84(%EC%83%81%EC%84%B8%EB%B3%B4%EA%B8%B0)"> 
 <img src="https://github.com/NohEuijin/RedMedicine2/assets/141835418/e34eeebf-bb32-4e68-a7aa-4497a9a10189?type=w580" width="500">
  <img src="https://github.com/NohEuijin/RedMedicine2/assets/141835418/6e8bd8bb-5efd-4eef-aacb-d7b98382b577?type=w580" width="500">
</a>

### 🔖상담예약 회원관리<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EC%83%81%EB%8B%B4%EC%98%88%EC%95%BD-%ED%9A%8C%EC%9B%90%EA%B4%80%EB%A6%AC"> WIKI로 이동</a>
- 상담사는 로그인시 드롭다운에 상담예약관리가 생김
- 상담 예약 관리 페이지에서는 이름, 상담날짜, 시간으로 검색이 가능(비동기)
- 해당 페이지는 10을 기준으로 페이징 처리(비동기)
- 상담을 예약한 회원의 정보를 번호와 이메일, 상담 내역을 모달창으로 확인 가능(비동기
- 상담취소 또는 상담완료 버튼으로 관리 가능

<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EC%83%81%EB%8B%B4%EC%98%88%EC%95%BD-%ED%9A%8C%EC%9B%90%EA%B4%80%EB%A6%AC"> 
 <img src="https://github.com/donghyun3193/SpringRedMedicine/assets/131939490/5e4ce5c3-d455-4ebc-a67d-7cf4b76e90ee?type=w580" width="500">
</a>

### 🔖제휴등록<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EC%A0%9C%ED%9C%B4%EB%93%B1%EB%A1%9D"> WIKI로 이동</a>
- 제휴를 등록하면 유료상담사가 됨
- 결제 버튼을 누르면 카카오페이로 결제가 진행
- 결제 진행은 비동기로 처리

<a href="https://github.com/NohEuijin/RedMedicine2/wiki/%EC%A0%9C%ED%9C%B4%EB%93%B1%EB%A1%9D"> 
 <img src="https://github.com/NohEuijin/RedMedicine2/assets/141835418/5a08dc2d-6aba-415d-8537-ddd1b034ec89?type=w580" width="500">
</a>
