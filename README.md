# 次世代家装后端项目
|-- .gitignore
|-- goods.json
|-- json.json
|-- mvnw
|-- mvnw.cmd
|-- pom.xml
|-- README.md
|-- .idea
|   |-- .gitignore
|   |-- compiler.xml
|   |-- encodings.xml
|   |-- jarRepositories.xml
|   |-- misc.xml
|   |-- vcs.xml
|   |-- workspace.xml
|   |-- ZeppelinRemoteNotebooks
|-- .mvn
|   |-- wrapper
|       |-- maven-wrapper.jar
|       |-- maven-wrapper.properties
|       |-- MavenWrapperDownloader.java
|-- libs
|   |-- java-sdk-core-3.2.0.jar
|-- search_result
|   |-- 2023年03月20日17时15分15秒.jpg
|-- src
|   |-- main
|   |   |-- java
|   |   |   |-- com
|   |   |       |-- homepainter
|   |   |           |-- HomePainterApplication.java
|   |   |           |-- config
|   |   |           |   |-- RedisConfig.java
|   |   |           |-- controller
|   |   |           |   |-- AlgorithmController.java
|   |   |           |   |-- CommunityController.java
|   |   |           |   |-- DataController.java
|   |   |           |   |-- DetectModelController.java
|   |   |           |   |-- DetectProductController.java
|   |   |           |   |-- FamilyController.java
|   |   |           |   |-- GoodsController.java
|   |   |           |   |-- HouseController.java
|   |   |           |   |-- HouseIdentifyController.java
|   |   |           |   |-- Image_Meta.java
|   |   |           |   |-- MultimodalController.java
|   |   |           |   |-- OrderController.java
|   |   |           |   |-- PictureBuilderController.java
|   |   |           |   |-- ShoppingController.java
|   |   |           |   |-- uploadImageController.java
|   |   |           |   |-- UserController.java
|   |   |           |   |-- UserDetailController.java
|   |   |           |   |-- DTO
|   |   |           |       |-- GoodsPlus.java
|   |   |           |       |-- InsertGoods.java
|   |   |           |-- filter
|   |   |           |   |-- BaseFilter.java
|   |   |           |   |-- GlobalExceptionHandler.java
|   |   |           |-- mapper
|   |   |           |   |-- AddressMapper.java
|   |   |           |   |-- Appraise_imageMapper.java
|   |   |           |   |-- CollectMapper.java
|   |   |           |   |-- EvaluateImageMapper.java
|   |   |           |   |-- GoodsMapper.java
|   |   |           |   |-- Goods_appraiseMapper.java
|   |   |           |   |-- Goods_imageMapper.java
|   |   |           |   |-- HotMapper.java
|   |   |           |   |-- OrderMapper.java
|   |   |           |   |-- TiebaEvaluateMapper.java
|   |   |           |   |-- TiebaFlagsMapper.java
|   |   |           |   |-- TiebaImageMapper.java
|   |   |           |   |-- TiebaMapper.java
|   |   |           |   |-- UserFurnitureMapper.java
|   |   |           |   |-- UserMapper.java
|   |   |           |-- pojo
|   |   |           |   |-- Address.java
|   |   |           |   |-- Appraise_image.java
|   |   |           |   |-- Collect.java
|   |   |           |   |-- EvaluateImage.java
|   |   |           |   |-- Goods.java
|   |   |           |   |-- Goods_appraise.java
|   |   |           |   |-- Goods_image.java
|   |   |           |   |-- Hot.java
|   |   |           |   |-- Order.java
|   |   |           |   |-- Tieba.java
|   |   |           |   |-- TiebaEvaluate.java
|   |   |           |   |-- TiebaFlags.java
|   |   |           |   |-- TiebaImage.java
|   |   |           |   |-- User.java
|   |   |           |   |-- UserFurniture.java
|   |   |           |   |-- DTO
|   |   |           |       |-- Imageinfos_withurl.java
|   |   |           |-- service
|   |   |           |   |-- Algorithm.java
|   |   |           |   |-- BehaveService.java
|   |   |           |   |-- CommunityService.java
|   |   |           |   |-- CommunityServiceImpl.java
|   |   |           |   |-- DataControlService.java
|   |   |           |   |-- DetectMLabelPro.java
|   |   |           |   |-- DetectProductBeta.java
|   |   |           |   |-- DrawImageService.java
|   |   |           |   |-- FileDownloader.java
|   |   |           |   |-- GoodsService.java
|   |   |           |   |-- GoodsServiceImpl.java
|   |   |           |   |-- HouseIdentify.java
|   |   |           |   |-- Multimodal.java
|   |   |           |   |-- OrderService.java
|   |   |           |   |-- OrderServiceImpl.java
|   |   |           |   |-- PictureBuilder.java
|   |   |           |   |-- Search_Service.java
|   |   |           |   |-- SpliteHouseService.java
|   |   |           |   |-- tencent_credential.java
|   |   |           |   |-- Test.java
|   |   |           |   |-- TranslateService.java
|   |   |           |   |-- Upload_File.java
|   |   |           |   |-- UserFurnitureService.java
|   |   |           |   |-- UserFurnitureServiceImpl.java
|   |   |           |   |-- UserService.java
|   |   |           |   |-- UserServiceImpl.java
|   |   |           |-- util
|   |   |               |-- Constant.java
|   |   |               |-- Father2Son.java
|   |   |               |-- File2Base64.java
|   |   |               |-- getStyleUtils.java
|   |   |               |-- get_Directory.java
|   |   |               |-- HouseIdentifyHandler.java
|   |   |               |-- HttpSSL.java
|   |   |               |-- MultipartFile2File.java
|   |   |               |-- object2map.java
|   |   |               |-- PaintImage.java
|   |   |               |-- RandomUtils.java
|   |   |               |-- ReadJson.java
|   |   |               |-- RecommandUtils.java
|   |   |               |-- RedisUtil.java
|   |   |               |-- Sample.java
|   |   |               |-- SendSms.java
|   |   |               |-- SSLCipherSuiteUtil.java
|   |   |               |-- time.java
|   |   |               |-- TokenUtil.java
|   |   |               |-- Translate.java
|   |   |               |-- UnsupportProtocolException.java
|   |   |               |-- ZipUtil.java
|   |   |-- resources
|   |       |-- application.properties
|   |       |-- application.yml
|   |       |-- goods.json
|   |       |-- model_info.json
|   |-- test
|       |-- java
|           |-- com
|               |-- homepainter
|                   |-- HomePainterApplicationTests.java
|                   |-- mapper
|                   |   |-- TestGoodsMapper.java
|                   |   |-- TestOrderMapper.java
|                   |   |-- testTieba.java
|                   |   |-- TestUserMapper.java
|                   |-- service
|                   |   |-- BuilderController.java
|                   |   |-- TestBehave.java
|                   |   |-- TestCommunityService.java
|                   |   |-- TestDataService.java
|                   |   |-- TestOrderService.java
|                   |   |-- TestPictureBuilder.java
|                   |   |-- TestUserService.java
|                   |-- util
|                       |-- TestRedis.java
|                       |-- TestSms.java
|                       |-- TestToken.java
|-- target
|-- classes
|   |-- application.properties
|   |-- application.yml
|   |-- goods.json
|   |-- model_info.json
|   |-- com
|       |-- homepainter
|           |-- HomePainterApplication.class
|           |-- config
|           |   |-- RedisConfig.class
|           |-- controller
|           |   |-- AlgorithmController.class
|           |   |-- CommunityController.class
|           |   |-- DataController.class
|           |   |-- DetectModelController.class
|           |   |-- DetectProductController.class
|           |   |-- FamilyController.class
|           |   |-- GoodsController.class
|           |   |-- HouseController.class
|           |   |-- HouseIdentifyController.class
|           |   |-- Image_Meta.class
|           |   |-- MultimodalController.class
|           |   |-- OrderController.class
|           |   |-- PictureBuilderController.class
|           |   |-- ShoppingController.class
|           |   |-- uploadImageController.class
|           |   |-- UserController.class
|           |   |-- UserDetailController.class
|           |   |-- DTO
|           |       |-- GoodsPlus.class
|           |       |-- InsertGoods.class
|           |-- filter
|           |   |-- BaseFilter.class
|           |   |-- GlobalExceptionHandler.class
|           |-- mapper
|           |   |-- AddressMapper.class
|           |   |-- Appraise_imageMapper.class
|           |   |-- CollectMapper.class
|           |   |-- EvaluateImageMapper.class
|           |   |-- GoodsMapper.class
|           |   |-- Goods_appraiseMapper.class
|           |   |-- Goods_imageMapper.class
|           |   |-- HotMapper.class
|           |   |-- OrderMapper.class
|           |   |-- TiebaEvaluateMapper.class
|           |   |-- TiebaFlagsMapper.class
|           |   |-- TiebaImageMapper.class
|           |   |-- TiebaMapper.class
|           |   |-- UserFurnitureMapper.class
|           |   |-- UserMapper.class
|           |-- pojo
|           |   |-- Address.class
|           |   |-- Appraise_image.class
|           |   |-- Collect.class
|           |   |-- EvaluateImage.class
|           |   |-- Goods.class
|           |   |-- Goods_appraise.class
|           |   |-- Goods_image.class
|           |   |-- Hot.class
|           |   |-- Order.class
|           |   |-- Tieba.class
|           |   |-- TiebaEvaluate.class
|           |   |-- TiebaFlags.class
|           |   |-- TiebaImage.class
|           |   |-- User.class
|           |   |-- UserFurniture.class
|           |   |-- DTO
|           |       |-- Imageinfos_withurl.class
|           |-- service
|           |   |-- Algorithm.class
|           |   |-- BehaveService.class
|           |   |-- CommunityService.class
|           |   |-- CommunityServiceImpl.class
|           |   |-- DataControlService.class
|           |   |-- DetectMLabelPro.class
|           |   |-- DetectProductBeta.class
|           |   |-- DrawImageService.class
|           |   |-- FileDownloader.class
|           |   |-- GoodsService.class
|           |   |-- GoodsServiceImpl.class
|           |   |-- HouseIdentify.class
|           |   |-- Multimodal.class
|           |   |-- OrderService.class
|           |   |-- OrderServiceImpl.class
|           |   |-- PictureBuilder.class
|           |   |-- Search_Service.class
|           |   |-- SpliteHouseService$1.class
|           |   |-- SpliteHouseService.class
|           |   |-- tencent_credential.class
|           |   |-- Test.class
|           |   |-- TranslateService.class
|           |   |-- Upload_File.class
|           |   |-- UserFurnitureService.class
|           |   |-- UserFurnitureServiceImpl.class
|           |   |-- UserService.class
|           |   |-- UserServiceImpl.class
|           |-- util
|               |-- Constant.class
|               |-- Father2Son.class
|               |-- File2Base64.class
|               |-- getStyleUtils.class
|               |-- get_Directory.class
|               |-- HouseIdentifyHandler.class
|               |-- HttpSSL.class
|               |-- MultipartFile2File.class
|               |-- object2map.class
|               |-- PaintImage.class
|               |-- RandomUtils.class
|               |-- ReadJson.class
|               |-- RecommandUtils.class
|               |-- RedisUtil.class
|               |-- Sample.class
|               |-- SendSms.class
|               |-- SSLCipherSuiteUtil$1.class
|               |-- SSLCipherSuiteUtil$TrustAllHostnameVerifier.class
|               |-- SSLCipherSuiteUtil$TrustAllManager.class
|               |-- SSLCipherSuiteUtil.class
|               |-- time.class
|               |-- TokenUtil.class
|               |-- Translate.class
|               |-- UnsupportProtocolException.class
|               |-- ZipUtil.class
|-- generated-sources
|-- annotations
