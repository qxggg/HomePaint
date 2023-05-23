<template>
	<view style="display: flex;flex-direction: column;">
				
		<view style="display: flex;flex-direction: row;margin-top: 44px;width: 95%;margin-left: 2%;">
			<image src="@/static/images/back.png" style="width: 20px;" mode="widthFix" @click="back()"></image>
			<search style="width: 85%;margin-left: 5px;"></search>
			<image src="@/static/ultis/scan.png" mode="widthFix" style="width: 30px;flex-shrink: 0;"
				@click="openCamera()"></image>
		</view>
		<view
			style="width: 100%;display: flex;flex-direction: row;justify-content: space-around;font-weight: bold;font-size: 20px;margin-top: 10px;">
			<view :class="{'active':now==0,'no-active':now==1}" @click="change_now({target:{current:0}})">
				<text>家具建模</text>
			</view>
			<view :class="{'active':now==1,'no-active':now==0}" @click="change_now({target:{current:1}})">
				<text>我的家具库</text>
			</view>
		</view>
		<swiper style="height: 100vh;" @change="change_now" :current="now">
			<swiper-item style="display: flex;flex-direction: column;height: 100%;">
				<view style="margin-top: 30px;">
					<uni-steps :options="list1" :active="active"></uni-steps>
				</view>
				<view v-if="active==0"
					style="width: 100%;display: flex;justify-content: center;flex-direction: column;line-height: 40px;align-items: center;margin-top: 15px;">
					<text style="font-size: 20px;font-weight: bold">家具建模</text>
					<text style="font-size: 14px;"> 对复杂场景中的单一物体进行建模</text>
					<image src="https://image-1304455659.cos.ap-nanjing.myqcloud.com/images/tip.jpg"
						style="width: 200px;" mode="widthFix"></image>
					<text style="font-size: 14px;color:  #3d5cff;"> 查看演示视频</text>

					<view class="group" @click="start">开始建模</view>
				</view>
				<view v-else-if="active==1" style="width: 90%;margin-left: 5%;;margin-top: 15px;">
					<uni-file-picker v-if="choose_image" v-model="imageValue" fileMediatype="image" mode="grid"
						:limit="limit"  @select="select" />
					<view v-else style="width: 100%;display: flex;justify-content: center;">
						<video autoplay="true" loop="true" id="myVideo"  :src="video_path" ></video>
					</view>
					
					<view style="width: 100%;display: flex;justify-content: center;flex-direction: column;line-height: 40px;align-items: center;margin-top: 15px;">
						<text style="font-size: 14px;color:  #3d5cff;" v-if="choose_image">建议拍摄约20张图片</text>
						<text style="font-size: 14px;color:  #3d5cff;" v-else>时间最多60s欧</text>
						<view class="group" @click="sendrequest('images')">开始模型渲染</view>
					</view>

				</view>

				<view v-else-if="active==2"
					style="display: flex;justify-content: center;flex-direction: column;text-align: center;">
					<text style="font-size: 20px;font-weight: bold;margin-top: 20px;">正在渲染</text>
					<view style="width: 95%;margin-left: 2%;margin-top: 20px;">
						<liu-progressbar :textInside="false" :progress="percent" color="#333333" :height="'40rpx'" />
					</view>
					<text style="font-size: 15px;font-weight: bold;margin-top: 20px;">模型渲染大概需要18分钟左右</text>
					<text style="font-size: 15px;font-weight: bold;margin-top: 20px;">(界面退出，渲染仍进行)</text>
					<text
						style="font-size: 15px;font-weight: bold;margin-top: 20px;">(采用前沿的InstantNeRF模型，建模过程耗费算力和时间巨大，请耐心等待)</text>
				</view>
			</swiper-item>

			<swiper-item>
				<scroll-view scroll-y="true" style="height: 1000px;margin-top: 10px;">
					<view class="recommend_goods">
						<view v-for="(item, index) in newlist" :key="index" class="goods">
							<view class="top" >
								<image class="cover" lazy-load="true" :src="item.imageUrl" mode="scaleToFill" @click="jump_result(item)"></image>
							</view>
							<view class="bottom">
								<view class="goods_name_1" >
									工程名:{{item.userFurniture.name}}
								</view>
								<view class="goods_name" >
									工程ID:{{item.userFurniture.fpId}}
									<image @click="delete_moule(item)" src="../../../../static/ultis/delete.png" style="width: 15px;float: right;" mode="widthFix"></image>
								</view>
								<view style="display: flex;align-items: center;justify-content: space-between;">
									<text class="text1">{{item.userFurniture.time.substring(0,10)}}</text>
									
									<view class="g_left" style="color: green;font-weight: bold;" v-if="item.userFurniture.diy==false">
										<text >家具商城</text>
									</view>
									<view class="g_left" style="color: red;font-weight: bold;" v-else-if="item.status==2||item.status==4">
										<text >已失败</text>
									</view>
									<view class="g_left" style="color: #3d5cff;" v-else-if="item.status==5">
										<text >处理中</text>		
									</view>
									<view class="g_left" style="color: green;" v-else-if="item.status==0">
										<text >建模成功！</text>		
									</view>
									<view class="g_left" style="color: yellow;" v-else-if="item.status==1">
										<text >待扫描</text>		
									</view>
									<view class="g_left" style="color: red;font-weight: bold;" v-else>
										<text >已失败</text>
									</view>
									

								</view>
							</view>
						</view>
					</view>
					<input  style="height: 250px;" disabled="true"/>
				</scroll-view>
				
			</swiper-item>
		</swiper>

		
	</view>
</template>


<script>
	const plugin=uni.requireNativePlugin('neoceansoft-videoThumbnail')
	import search from "@/pages/commponent/public/search.vue";
	import {
		pathToBase64
	} from '@/utils/image-tools/index.js'
	export default {
		data() {
			return {
				percent: 0,
				duration: 1000,
				active: 0,
				sourceType: ['camera'],
				list1: [{
					title: '填写名称'
				}, {
					title: '拍照/录像'
				}, {
					title: '服务器渲染'
				}, {
					title: '完成'
				}],
				data: null,
				now: 0,
				name: '',
				choose_image: false,
				imageValue: [],
				imageURL: [],
				video_value: null,
				image_src: '',
				newlist:[],
				status:[
					{}
				],
				limit:30,
				video_path:null,
				ctx:null,
				fp_id:null,
				interval:null
			}
		},
		components: {
			search
		},
		mounted(){
			this.init_data();
			
		},
		onPullDownRefresh() {
			this.init_data();
			uni.stopPullDownRefresh();
		},
		methods: {
			back(){
				uni.navigateBack();
			},
			delete_moule(item){
				uni.showModal({
					title:'确认删除该模型吗?',
					confirmText:'删除',
					success: (res) => {
						console.log(res);
						if(res.confirm){
							var temp = {
								fp_id:item.userFurniture.fpId
							};
							this.request(this.server_url+'Module/delete',temp,'POST').then((res)=>{
								console.log(res);
								if(res.code==0){
									uni.showToast({
										title:'删除成功!'
									});
									this.init_data();
								}
							});
						}
					}
				});
			},
			init_data(){
				this.request(this.server_url+'Module/list',{},'GET').then((res)=>{
					console.log(res);
					if(res.code==0){
						this.newlist = res.data;
					}
				})
			},
			jump_result(item){
				if(!item.userFurniture.diy){
					console.log(item)
					var temp = {
						OBJ:'https://image-1304455659.cos.ap-nanjing.myqcloud.com/3D-FUTURE-model-part1/'+item.userFurniture.fpId+'/normalized_model.obj',
						jpg:'https://image-1304455659.cos.ap-nanjing.myqcloud.com/3D-FUTURE-model-part1/'+item.userFurniture.fpId+'/texture.png',
						mtl:'https://image-1304455659.cos.ap-nanjing.myqcloud.com/3D-FUTURE-model-part1/'+item.userFurniture.fpId+'/model.mtl',
						scale:500
					};
					
					uni.setStorageSync('result_file',temp);
					uni.navigateTo({
						url:'/pages/navigation/ShouYe/furniture_modeling/model_result'
					});
					return;
				}
				if(item.status!=0){
					uni.showToast({
						title:'尚未成功!',
						icon:'none'
					});
					return;
				}
				
				// uni.showLoading({
				// 	title:'获取中...'
				// })
				var temp = {
					fp_id:item.userFurniture.fpId
				};
				uni.showLoading({
					title:'加载中...'
				})
				this.request(this.server_url+'Module/download',temp,'POST').then((res)=>{
					uni.hideLoading();
					console.log(res);
					if(res.code==0){
						uni.setStorageSync('result_file',res.data);
						uni.navigateTo({
							url:'/pages/navigation/ShouYe/furniture_modeling/model_result'
						})
					}
				});
				

			},
			change_now(e) {
				this.now = e.target.current;
			},
			select_video(e) {
				console.log(e);
				console.log(this.imageValue);
			},
			select(e) {
				// console.log(e);
				var that = this;
				this.image_src = e.tempFilePaths[0];
				pathToBase64(e.tempFilePaths[0]).then(data => {
					var temp = {
						image: data
					};

					that.request(this.server_url + 'upload_image', temp, 'POST').then((res) => {
						// console.log(res);
						that.imageURL.push(res.data);
					});
				})


			},
			start() {
				uni.showModal({
					title: '创建家具名称:',
					editable: true,
					success: (write_name) => {
						console.log(write_name);

						if (write_name.confirm && write_name.content.length != 0) {
							this.name = write_name.content;
							
							uni.showModal({
								content: '选择建模方式',
								confirmText: '拍照',
								cancelText: '录像',
								success: (re) => {
									console.log(re);
									if (re.confirm) {
										this.active = 1;
										this.choose_image = true;
									} else {
										uni.chooseVideo({
											camera:'front',
											compressed:false,
											success: (video) => {
												console.log(video);
												if(video.errMsg=='chooseVideo:ok'){
													this.ctx = uni.createVideoContext('myVideo');
													this.video_path = video.tempFilePath;
													this.choose_image = false;
													this.active = 1;
												}else{
													uni.showToast({
														title:video.errMsg,
														icon:'none'
													});
												}

											}
										});

									}
								}
							})
						}else{
							uni.showToast({
								title:'请填写名称',
								icon:'none'
							});
						}

					}
				})
			},
			sendrequest(type) {
				var that = this;
				if(this.choose_image==false){
					console.log('--------------------')

					
					// 获取视频封面
					plugin.getFrame(
						plus.io.convertLocalFileSystemURL(this.video_path),
						1,
						(res)=>{
							console.log(res);
							
							
							if(res.code==0){
								
								// 封面图片转base64
								pathToBase64(res.imagePath).then(data => {									
									// 上传视频
									uni.showLoading({
										title:'上传视频中!'
									});
									this.ctx.stop();
									uni.uploadFile({
										url:that.server_url+'Module/CreateMoudleVideoWithImage',
										filePath:that.video_path,
										header:{
											"token":uni.getStorageSync('token')
										},
										name:'video',
										formData:{
											"projectName":that.name,
											'image':data
										},
										timeout:1000000,
										complete: (res) => {
											uni.hideLoading();
											this.active = 2;
											console.log(res);
											if(res.code!=0){
												uni.showToast({
													title:'上传成功！',
													icon:'none'
												});
											}
											var that = this;
											that.interval = setInterval(function() {
												that.percent += 1;
												if(that.percent>=100){
													clearInterval( that.interval );
												}
											}, 5000);
										}
									})
								})
								

							}
						}
					);
					

					return;
				}
				if (type == 'images') {
					var temp = {
						type,
						images: this.imageURL,
						projectName:that.name
					}
					console.log(temp);
					uni.showLoading({
						title: '上传中...'
					})
					this.request(this.server_url + 'Module/CreateMoudle', temp, 'POST').then((res) => {
						console.log(res);
						this.swiperList = res.data;
						uni.hideLoading();
						if (res.code == 0) {
							this.active = 2;
							var that = this;
							that.interval = setInterval(function() {
								that.percent += 1;
								if(that.percent>=100){
									clearInterval( that.interval );
								}
							}, 5000);
						}
					});
				} else {
					temp = {
						...temp,
						video: {}
					};
				}

			}
		}

	}
</script>

<style>
	.recommend_goods {
		padding: 20upx;
		column-count: 2;
		/*分为两列  用于瀑布流*/
		column-gap: 20upx;
	}
	.group {
		display: flex;
		flex-direction: row;
		align-items: center;
		justify-content: center;
		background-color: #3d5cff;
		width: 60%;
		border-radius: 7px;
		color: white;
		font-weight: bold;
		font-size: 15px;
	}

	.active {
		color: #3d5cff;
		text-decoration: underline;
	}

	.no-active {
		color: grey;
	}
	
	.goods {
		height: 100%;
		overflow: auto;
		margin-bottom: 20upx;
		break-inside: avoid;
		/*用于瀑布流*/
		border-radius: 10upx;
		box-sizing: content-box;
	
		&:first-child {
			margin-top: 0;
		}
	}
	
	.goods .top {
		height: 45vw;
		overflow: hidden;
		position: relative;
		background-color: #ffffff;
	}
	
	.top .cover {
		width: 100%;
		height: 100%;
		display: flex;
		align-items: center;
	}
	
	.top .tags {
		width: 65upx;
		height: 65upx;
		position: absolute;
		top: 0;
		left: 0;
	}
	
	.bottom {
		padding: 15upx;
		background-color: #ffffff;
		overflow: hidden;
	}
	
	.goods_name {
		/* height: 66upx; */
		font-size: 24upx;
		word-break: break-all;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		font-weight: bold;
	}
	
	.goods_name_1 {
		/* height: 66upx; */
		font-size: 30upx;
		word-break: break-all;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		font-weight: bolder;
	}
	
	.bottom .price {
		margin-top: 15upx;
	}
	
	.bottom .price .text1 {
		font-size: 32upx;
		font-weight: bold;
		color: '#fa436a';
	}
	
	.bottom .price .text2 {
		font-size: 22upx;
		color: #a0a0a0;
		text-decoration: line-through;
		padding-left: 15upx;
	}
	
	.goumai {
		margin-top: 10upx;
	}
	
	.g_left {
		font-size: 24upx;
		float: left;
		align-items: center;
	}
	

	
	.g_right {
		float: right;
		font-size: 28upx;
		color: '#fa436a';
	}
	
	.mask {
		width: 100%;
		height: 100%;
		position: fixed;
		top: 0;
		left: 0;
		background: #000;
		z-index: 900;
		opacity: 0.7;
	}
	
	.sku {
		width: 100vw;
		min-height: 30vh;
		position: fixed;
		bottom: -100%;
		z-index: 910;
		left: 0;
		background-color: #ffffff;
		padding: 20upx 4%;
		border-top-left-radius: 10upx;
		border-top-right-radius: 10upx;
		border-bottom: 1upx solid #eee;
		transition: all 0.3s;
	}
	
	.shows {
		/* #ifdef MP */
		bottom: 0 !important;
		/* #endif */
		/* #ifdef H5 */
		bottom: 100upx !important;
		/* #endif */
		transition: all 0.3s;
	}
	
	.sku_top {
		overflow: hidden;
	}
	
	.sku_top image {
		height: 170upx;
		width: 170upx;
		float: left;
		margin-right: 15upx;
		border-radius: 8upx;
	}
	
	.sku_top .sku_title {
		font-size: 24upx;
		line-height: 35upx;
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		font-weight: bold;
	}
	
	.sku_top .moneys {
		font-size: 28upx;
		line-height: 40upx;
		overflow: hidden;
		margin-top: 20upx;
	}
	
	.sku_top .kucun {
		font-size: 24upx;
		color: #999;
		overflow: hidden;
	}
	
	.sku_list {
		margin-top: 20upx;
		overflow: hidden;
		margin-bottom: 40upx;
	}
	
	.sku_name {
		font-size: 24upx;
		color: #666;
		overflow: hidden;
	}
	
	.sku_tag {
		overflow: hidden;
		margin-top: 20upx;
	}
	
	.sku_tag .tag_s {
		height: 50upx;
		line-height: 50upx;
		padding: 0 20upx;
		text-align: center;
		font-size: 22upx;
		color: #333;
		background-color: #F5F5F5;
		border: 1upx solid rgba(0, 0, 0, .05);
		float: left;
		border-radius: 30upx;
		margin-right: 20upx;
		transition: all 0.2s;
		margin-bottom: 20upx;
	}
	
	.number {
		margin-top: 10upx;
		border-top: 1upx solid #ccc;
		width: 100%;
		height: 80upx;
		line-height: 80upx;
		padding-top: 10upx;
	}
	
	.number .n_left {
		float: left;
		font-size: 28upx;
		color: #333;
	}
	
	.number .n_right {
		float: right;
		height: 60upx;
		width: 200upx;
		background-color: #F5F5F5;
		margin-top: 10upx;
		border-radius: 5upx;
	}
	
	.n_right .jian,
	.jia {
		width: 60upx;
		height: 60upx;
		text-align: center;
		line-height: 60upx;
		font-size: 42upx;
	}
	
	.jian {
		float: left;
	}
	
	.jia {
		float: right;
	}
	
	.jian:active {
		background-color: #eee;
	}
	
	.jia:active {
		background-color: #eee;
	}
	
	.n_right input {
		width: 76upx;
		float: left;
		text-align: center;
		margin-top: 6upx;
	}
	
	.btn_box {
		margin-top: 40upx;
	}
	
	.btn_box .addcart_btn,
	.submit {
		width: 40vw;
		height: 56upx;
		line-height: 56upx;
		border-radius: 42upx;
		font-size: 24upx;
		text-align: center;
		color: #ffffff;
		float: left;
		margin-left: 30upx;
		margin-bottom: 30upx;
	}
	
	.btn_box view:active {
		opacity: 0.8;
	}
	
	/* 列表类样式 */
	.list_mode {
		padding: 20upx 4% 0upx;
		z-index: 10;
		background-color: #FFFFFF;
	}
	
	.goods_list {
		overflow: hidden;
	}
	
	.goods_list .goods_item {
		align-items: center;
		border-bottom: 1upx solid #eee;
		padding-bottom: 10upx;
		margin-bottom: 15upx;
		overflow: hidden;
		position: relative;
	
		&:last-of-type {
			border-bottom: none;
		}
	}
	
	.goods_item image {
		width: 200upx;
		height: 200upx;
		float: left;
		border-radius: 10upx;
		margin-right: 5upx;
	}
	
	.goods_item .tags {
		width: 60upx;
		height: 60upx;
		position: absolute;
		top: 0;
		left: 0;
	}
	
	.goods_right {
		/* float: left; */
		padding: 0 10upx;
		overflow: hidden;
	}
	
	.goods_right .goods_name {
		font-size: 28upx;
		overflow: hidden;
		font-weight: bold;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		color: #333;
		min-height: 50upx;
	}
	
	.goods_right .numbers {
		font-size: 20upx;
		line-height: 30upx;
		overflow: hidden;
		margin-top: 30upx;
		display: flex;
		align-content: center;
	}
	
	.goods_right .numbers text {
		display: inline-block;
		height: 35upx;
		line-height: 35upx;
		padding: 0 10upx;
		background-color: #FAEFF7;
		border-radius: 10upx;
		margin-right: 20upx;
		color: '#fa436a';
	}
	
	.goods_right .price {
		line-height: 40upx;
		font-size: 24upx;
		overflow: hidden;
		margin-top: 20upx;
	}
	
	.price .money {
		margin-right: 20upx;
		font-size: 32upx;
		font-weight: bold;
		color: '#fa436a';
	}
	
	.hx_money {
		text-decoration: line-through;
		color: #999;
		font-size: 22upx;
	}
	
	.gouwuche {
		font-size: 32upx;
		float: right;
		margin-right: 20upx;
		color: '#fa436a';
	}
	
	.nodata {
		color: #999;
		text-align: center;
		font-size: 24upx;
		margin-top: 20upx;
		height: 80upx;
		line-height: 80upx;
	}
</style>
