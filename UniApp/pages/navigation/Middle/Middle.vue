<template>
	<view style="display: flex;flex-direction: column;width: 100%;height: 100%;">
		<view class="container" >
			<view style="width: 90%;margin-top: 120rpx;display: flex;flex-direction: column;margin-left: 5%;text-align: left;;">
				<text class="title">Virtual </text>
				<text class="title">Decoration</text>
			</view>
			

			<image src="@/static/navgiation/middle/vr.png" mode="widthFix" style="width: 80%;margin-top: 280rpx;position: fixed;z-index: 1;"></image>
			
			<view style="width: 100%;display: flex;justify-content: center;z-index: 2;margin-top: 400rpx;">
				<wButton
					class="button_start"
					text="连接VR"
					:rotate="rotate" 
					@click="start_rotate"
				></wButton>
			</view>

			<view style="width: 90%;margin-top: 250rpx;display: flex;flex-direction: column;margin-left: 5%;z-index: 2;">
				<text class="title">开启VR虚拟世界</text>
				<text class="fu_title" style="margin-top: 10rpx;">在VR虚拟世界中你可以置身你的房屋中，并对你的房屋风格、家具摆放等进行调整！</text>
			</view>
			

		</view>
		<tabbar :nowpage="nowpage"></tabbar>
	</view>

</template>

<script>
	import wButton from '@/components/watch_button/watch-button.vue' //button
	export default {
		data() {
			return {
				nowpage:1,
				statusBarHeight:0,
				history:[],
				search_context:'',
				rotate:false
			}
		},
		components:{wButton},
		onLoad() {
			if(uni.getStorageSync('search_history')!=false){
				this.history = uni.getStorageSync('search_history');
			}
			this.user=uni.getStorageSync('user');
			this.statusBarHeight = uni.getSystemInfoSync().statusBarHeight;	
		},
		methods: {
			start_rotate(){
				console.log('------------')
				this.rotate = true;
				var that = this;
				setTimeout(function(){
					that.rotate = false;
					uni.showToast({
						title:'未检测到VR设备',
						icon:'none'
					});
				},5000);
			},
			search(){
				this.history[3]=this.search_context;
				// uni.setStorageSync('search_history',this.history);
				this.search_context = '';
			},
			change_nowpage(e){
				this.nowpage=e;
				if(this.nowpage==0){
					uni.navigateTo({
						url:'/pages/navigation/ShouYe/ShouYe'
					})
				}else if(this.nowpage==2){
					uni.navigateTo({
						url:'../WoDe/WoDe'
					})
				}
			},
			jump_exam(){
				uni.navigateTo({
					url:'/pages/navigation/WoDe/exam_list/exam_list'
				})
			},
			jump_lianxi(){
				uni.navigateTo({
					url:'/pages/navigation/WoDe/cuoti_list/cuoti_list'
				})
			},
			Unlogin(){
				uni.clearStorageSync();
				uni.clearStorage();
				uni.reLaunch({
					url:'../../login/login'
				});
			}
		}
	}
</script>


<style>
	@import url("../tabbar.css");
	hr{
		border: 1px solid rgb(234, 230, 230);
	}
	
	page{
		width : 100%;
		height: 100%;
		background: url('../../../static/navgiation/middle/bac.png');
		background-size: 100% 100%;
	}
	
	.title{
		font-size: 28px;
		color: #4244ff;
		letter-spacing: 0;
		font-weight: 600;
	}
	
	.fu_title{
		font-size: 14px;
		color: #5F5F5F;
		letter-spacing: 0;
		line-height: 20px;
		font-weight: 600;
		margin-top: 5rpx;
	}
	
	.container{

		height: 70%;
	}
	

</style>
