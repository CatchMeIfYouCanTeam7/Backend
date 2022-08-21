# Catch me if you can !
캐치마인드를 모두 함께 즐기자 !<br>
<br><br>



## 프로젝트 개요

1.👋프로젝트 소개<div> 
캐치마인드를 여러 사람들과 함께 문제도 내고 정답을 맞춰보는 자유로운 공간 입니다.<br>
여러분들의 상상력을 발휘해 문제도 출제하고 정답도 맞춰 보세요!
<br><br>

2.⏳ 프로젝트 기간
<div> <strong>2022년 8월 12일 ~ 2022년 8월 18일 </strong></div>
<br>
3. 👨‍👩‍👧‍👦프로젝트 멤버<div>
FrondEnd(React)<br> 
배아랑이<br>
나소나<br>
김민석<br><br>
Backend(Spring)<br>
김보슬<br>
문경록<br>
<br>

## 구현 기능

>1. 회원가입/로그인
- Spring Security , JWT인증박싱을 사용하여 로그인기능 구현 

>2. 문제출제 
- 문제 목록 조회,등록,수정,삭제

- 상세조회
- 이미지등록,수정,삭제

>3. 댓글 
- 댓글 조회,등록,삭제
 
# :tv: 데모영상
<img src="https://img.shields.io/badge/YouTube-FF0000?style=flat&logo=YouTube&logoColor=white"/>https://youtu.be/vf16mWAeWBM
 
# :computer: 기술 스택 
#### Server 
  <img src="https://img.shields.io/badge/aws-232F3E?style=for-the-badge&logo=AmazonAWS&logoColor=white">
  
#### Framework
  <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"><img src="https://img.shields.io/badge/Springboot-6DB33F?style=for-the-badge&logo=Springboot&logoColor=white">
  
#### Language
  <img src="https://img.shields.io/badge/JAVA-007396?style=for-the-badge&logo=java&logoColor=white">
  
#### Database
  <img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white">
  
#### Tool
  <img src="https://img.shields.io/badge/gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white"><img src="https://img.shields.io/badge/Git-00000?style=for-the-badge&logo=Git&logoColor=F05032]"/><img src="https://img.shields.io/badge/Github-181717?style=for-the-badge&logo=Github&logoColor=white]"/>

# :key: 트러블 슈팅
> 1)  파일업로드<br>

S3 사진업로드를 테스트 하는 과정에서 macOS에서 찍은 일부 스크린샷 파일들이 올라가지않는것을 확인<br> 

문제해결*
<br>String fileName = awsS3Service.upload(multipartFile);
<br>String imgUrl = URLDecoder.decode(fileName, "UTF-8");
<br>기존에는 s3에서 업로드시 변환된 imgurl을 그대로 사용했는데 파일이 올라가는 과정에서 변환된 파일명에서 오류가 생긴걸 확인하고,
<br>받아온 url을 한번더 변환을 통해 올라가지 않던 파일들을 정상적으로 올라가게 해결.
> 2) CORS<br>

서비스를 하는과정에 CORS 설정을 했지만 일부 오류가 발생하는 현상이 나타남
<br>기존에 WebConfig에서 addCorsMappings를  이용하여 CORS 문제가 일어나지 않았지만 추가로 기능등을 확인하는 과정에서 발생하였음.<br>
*문제해결
<br>SecurityConfiguration에 CorsConfigurationSource를 이용하여 라이브러리를 주입하여 배포를 해보니 CORS를 해결.




 
 
