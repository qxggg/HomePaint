<template>
	<view class="header" :style="{background: colors}" style="display: flex;flex-direction: column;">


		<view class="status-bar" style="margin-top: 20px;"></view>

		<view>
			<view class="left" @tap="nearAddress">
				<image src="@/static/images/back.png" style="width: 20px;" mode="widthFix" @click="back()"></image>
				<image src="/static/images/home/weizhi.png"></image>
				<view>{{city || '请选择位置'}}</view>
			</view>
			<view class="right" v-if="weatherName && weatherName!==''">
				<image :src="todyWeather.img"></image>
				<text>{{ weatherName }} /{{ high }}℃</text>
			</view>
		</view>
		<!-- 搜索 -->
		<view style="display: flex;flex-direction: row;">
			<search style="width: 90%;"></search>
			<image src="../../../static/ultis/saowu.png" mode="widthFix" style="width: 30px;flex-shrink: 0;"
				@click="openCamera()"></image>
		</view>

		<!-- 轮播图 -->
		<view class="swiper">
			<view class="swiper-box">
				<swiper circular="true" @change="swiperChange" previous-margin="25px" next-margin="25px">
					<swiper-item @click="jump_search(item)" v-for="(item, index) in swiperList" :key="index">
						<image :src="item.img" mode="aspectFill" :class="currentSwiper !== index ?'swiper-item-side':''"
							lazy-load="true"></image>
					</swiper-item>
				</swiper>
				<view class="indicator">
					<view v-for="(item, index) in swiperList" :key="index"
						:class="currentSwiper >= index ? 'on' : 'dots'"
						:style="'width: ' + (currentSwiper >= index ? 100 / swiperList.length + '%' : '' )"></view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import {
		pathToBase64,
		base64ToPath
	} from '@/utils/image-tools/index.js'

	import qqmapsdk from "../../../utils/qqmap-wx-jssdk.min";
	import search from "../public/search.vue";
	import {
		hfKey,
		txMapKey
	} from '../../../utils/keys.js'

	export default {
		data() {
			return {
				currentSwiper: 0,
				city: '北京',
				weatherData: [{
					type: '阴',
					img: "/static/weather/yin.png"
				}, {
					type: '晴',
					img: "/static/weather/qing.png"
				}, {
					type: '多云',
					img: "/static/weather/duoyun.png"
				}, {
					type: '雨',
					img: "/static/weather/xiaoyu.png"
				}, {
					type: '小雨',
					img: "/static/weather/xiaoyu.png"
				}, {
					type: '中雨',
					img: "/static/weather/xiaoyu.png"
				}, {
					type: '大雨',
					img: "/static/weather/dayu.png"
				}, {
					type: '暴雨',
					img: "/static/weather/leiyu.png"
				}, {
					type: '雷阵雨',
					img: "/static/weather/leiyu.png"
				}, {
					type: '雨夹雪',
					img: "/static/weather/xiaoxue.png"
				}, {
					type: '雪',
					img: "/static/weather/xue.png"
				}, {
					type: '小雪',
					img: "/static/weather/xue.png"
				}, {
					type: '中雪',
					img: "/static/weather/xiaoxue.png"
				}, {
					type: '大雪',
					img: "/static/weather/daxue.png"
				}],
				high: '',
				weatherName: '',
				latitude: '',
				longitude: '',
				todyWeather: {},
				statusBarHeight: 20
			};
		},

		components: {
			search
		},
		props: {
			colors: {
				type: String
			},
			locations: {
				type: Object
			},
			swiperList: {
				type: Array
			}
		},
		created() {
			console.log('key', hfKey, txMapKey)
			// #ifndef H5
			this.getUserPosition();
			// #endif
			// #ifdef H5
			if (this.locations.latlng) {
				this.latitude = this.locations.latlng.lat || ''
				this.longitude = this.locations.latlng.lng || ''
				this.city = this.locations.poiname
				this.getWeather(this.latitude, this.longitude);
			}
			// #endif
		},
		watch: {
			locations(value) {
				this.latitude = this.locations.latlng.lat || ''
				this.longitude = this.locations.latlng.lng || ''
				this.city = this.locations.poiname
				this.getWeather(this.latitude, this.longitude);
			}
		},
		methods: {
			back(){uni.navigateBack()},
			jump_search(e) {
				uni.navigateTo({
					url: '/pages/navigation/ShouYe/furniture_shop/classList?content=' + e.title
				})
			},
			openCamera() {
				uni.chooseImage({
					count: 1,
					success: (res) => {
						pathToBase64(res.tempFilePaths[0]).then(data => {
							var temp = {
								image: data
							}
							uni.showLoading({
								title: '多模态检索中'
							});
							this.request(this.server_url + 'MultiModal', temp, 'POST').then((res) => {
								console.log(res);
								uni.hideLoading();
								uni.setStorageSync('search_result', res);
								uni.navigateTo({
									url: '/pages/shop/goods/MutiModel'
								});
							})
						})

					}
				})
			},
			getUserPosition() {
				/**
				 * 在这里执行获取用户的位置
				 */
				//获取用户位置
				let that = this;
				uni.getLocation({
					type: 'wgs84',
					geocode: true,
					success: res => {
						console.log('获取经纬度成功', res);
						that.setData({
							latitude: res.latitude,
							longitude: res.longitude
						});
					},
					fail: () => {
						that.setData({
							latitude: '',
							longitude: ''
						});
					},
					complete: () => {
						// 解析地址
						that.getCity();
					}
				});
			},

			getCity() {
				const QQMapWX = new qqmapsdk({
					//腾讯地图  需要用户自己去申请key
					key: txMapKey,
					sig: 'K9NR556vsOa9PYtpWpYV1hJ8tiCgRmK'
				});
				let that = this;
				QQMapWX.reverseGeocoder({
					location: {
						latitude: that.latitude,
						longitude: that.longitude
					},
					success: function(res) {
						console.log('解析地址成功', res);
						let province = res.result.ad_info.province; // 市

						let city = res.result.address;
						that.setData({
							city: city
						});
						let position = {};
						position.lat = that.latitude;
						position.lng = that.longitude;
						position.name = city;
						that.getWeather(that.latitude, that.longitude);
					},
					fail: function(res) {
						console.log(res);
					},
					complete: function(res) {
						console.log(res);
					}
				});
			},
			getWeather(la, lo) { //获取天气信息
				console.log('--------------------')
				let url = 'https://devapi.qweather.com/v7/weather/now?key=' + hfKey + '&location=' + lo + ',' + la;
				uni.request({
					url: url,
					method: 'GET',
					success: res => {
						console.log(res);
						let today = res.data.now;

						let h = today.temp;
						this.setData({
							high: h,
							//高温
							weatherName: today.text
						});

						this.weatherData.forEach(e => {
							if (e.type == today.text) {
								this.setData({
									todyWeather: e
								});
							}
						});

						if (this.todyWeather.type == '' || !this.todyWeather.type) {
							let data = this.weatherData[0];
							this.setData({
								todyWeather: data
							});
						}
					},
					fail: () => {},
					complete: () => {}
				});
			},

			nearAddress() { //设置位置信息
				// #ifdef MP
				const _this = this; // 处理拒绝再次打开调用设置
				uni.getSetting({
					success(res) {
						if (res && res.authSetting && res.authSetting.hasOwnProperty('scope.userLocation')) {
							uni.openSetting({
								success() {
									_this.getUserPosition();
								}
							});
						}
					}
				});
				// #endif
				// #ifdef H5
				uni.navigateTo({
					url: '/pages/views/home/h5map'
				})
				// #endif
			},
			swiperChange(e) {
				this.setData({
					currentSwiper: e.detail.current,
				});
			}
		}
	};
</script>
<style scoped lang="scss">
	.header {
		padding: 0 3%;
		line-height: 80upx;
		overflow: hidden;
		height: 555upx;
		color: #fff;
		position: relative;
	}

	.place {
		position: absolute;
		width: 100%;
		height: 100%;
		top: 0;
		left: 0;
		background: linear-gradient(rgba(255, 255, 255, 0), rgba(255, 255, 255, .3), rgba(255, 255, 255, .5), #ffffff);
	}

	.left {
		font-size: 26upx;
		color: #333;
		float: left;
		height: 80upx;
		color: #000;
		display: flex;
		align-items: center;
		align-items: center;
		z-index: 800;
	}

	.left image {
		width: 30upx;
		height: 30upx;
		float: left;
		margin-right: 6upx;
	}

	.left view {
		width: 60vw;
		height: 30upx;
		line-height: 30upx;
		position: relative;
		overflow: hidden;
		white-space: nowrap;
		text-overflow: ellipsis;
	}

	.right {
		height: 80upx;
		float: right;
		font-size: 26upx;
		display: flex;
		align-items: center;
	}

	.right image {
		width: 40upx;
		height: 40upx;
	}

	.right text {
		margin-left: 10upx;
	}

	.swiper {
		width: 100%;
		margin-top: 10upx;
		display: flex;
		justify-content: center;
	}

	.swiper-box {
		width: 100%;
		height: 45vw;
		overflow: hidden;
		/* border-radius: 15upx; */
		/* box-shadow: 0upx 8upx 25upx rgba(0, 0, 0, 0.2); */
		position: relative;
		z-index: 1;
	}

	.swiper-box swiper {
		width: 100%;
		height: 45vw;
	}

	.swiper-box swiper swiper-item {
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.swiper-box swiper swiper-item image {
		width: 95%;
		height: 45vw;
		margin: 0 auto;
		display: block;
		border-radius: 10px;
		transition: height .3s;
	}

	.swiper-item-side {
		width: 95%;
		height: 40vw !important;
		transition: height .3s;
	}

	.indicator {
		position: absolute;
		bottom: 20upx;
		left: 20upx;
		background-color: rgba(255, 255, 255, 0.4);
		width: 150upx;
		height: 5upx;
		border-radius: 3upx;
		overflow: hidden;
		display: flex;
	}

	.dots {
		width: 0upx;
		background-color: rgba(255, 255, 255, 1);
		transition: all 0.3s ease-out;
	}

	.on {
		width: 30%;
		background-color: rgba(255, 255, 255, 1);
		transition: all 0.3s ease-out;
	}
</style>
