<template>
	<view>
		<navBar :showLeft="true" navTitle="我的收藏" textcolor="#000" bgColor="#f8f8f8" :leftBg="false"></navBar>
		<view class="mycollection">
			<view class="cart_box">
				<view v-for="(item, index) in dataList" :key="index" class="cart_list" @longtap="onshowDel(item,index)"
					@touchend="ontouchend">
					<view class="cover" @tap="jumpDetails(item,index)">
						<image v-if="item.imageUrl!=null" :src="item.imageUrl[0].imageUrl" mode="aspectFill"></image>
					</view>
					<view class="right">
						<view class="goods_name" @tap="jumpDetails(item,index)">
							{{item.title}}
						</view>
						<view class="sku">
							收藏于：{{item.time.substring(0,10)}}
						</view>
						<view class="numbers">
							<text class="price">
								￥{{item.price}}
							</text>
						</view>
					</view>
					<!-- 删除的遮罩层 长按触发 -->
					<view class="del_mask" v-if="current == index" :style="'z-index:' + (current == index ?'99':'-21')"
						@tap="oncencal">
					</view>
					<view class="dask_del"
						:style="'opacity:' + (current == index ?'1':'0') + ';z-index:' + (current == index ?'20':'-20')+';left:'+(current == index?'0':'-100%')">
						<text class="del" @tap="delItem(item,index)">删除</text>
						<text class="cencal" @tap="oncencal">取消</text>
					</view>
				</view>
				<view class="daodi" v-if="dataList.length >= 5">—— 到底啦 ——</view>
				<nodata :colors="colors" title="暂无收藏" v-if="dataList.length == 0"></nodata>
			</view>
		</view>
		<loading v-if="isShow == true"></loading>
	</view>
</template>

<script>
	var app = getApp();
	import loading from "@/pages/commponent/public/loading";
	import navBar from '@/pages/commponent/public/navBar.vue'
	export default {
		data() {
			return {
				colors: '',
				dataList: [],
				current: '99999',
				lock: false,
				isShow: true
			};
		},

		components: {
			loading,
			navBar
		},
		props: {},

		/**
		 * 生命周期函数--监听页面加载
		 */
		onLoad: function(options) {
			this.setData({
				colors: app.globalData.newColor
			});
		},
		mounted() {
			this.request(this.server_url+'goods/shoucang/list',{},'GET').then((res)=>{
				console.log(res);
				this.isShow = false;
				for(var i=0;i<res.goods.length;i++){
					this.dataList.push({...res.goods[i].goods,time:res.goods[i].time});
				}
				
			})
		},
		methods: {
			onshowDel(item, index) {
				//显示删除
				this.setData({
					current: index,
					lock: true
				});
				return;
			},

			delItem(item, index) {
				//点击删除
				console.log('删除成功');
			},

			oncencal() {
				//点击取消
				this.setData({
					current: '99999'
				});
			},

			ontouchend() {
				if (this.lock) {
					setTimeout(() => {
						this.setData({
							lock: false
						});
					}, 100);
				}
			},

			jumpDetails(item, index) {
				console.log(item)
				//跳转详情
				if (this.lock) {
					return;
				}
				uni.setStorageSync('goodsDetail',item);
				uni.navigateTo({
					url: '/pages/shop/goods/goodsDetails'
				});
			},
		}
	};
</script>
<style scoped lang="scss">
	.mycollection {
		padding: 10upx 4%;
	}

	.cart_box {
		margin-top: 20upx;
		padding-bottom: 100upx;
	}

	.cart_list {
		width: 92vw;
		height: calc(22vw + 34upx);
		border-radius: 12upx;
		box-shadow: 0px 4upx 16upx rgba(0, 0, 0, .1);
		z-index: 4;
		overflow: hidden;
		border: 0;
		display: flex;
		align-items: center;
		position: relative;
		margin-bottom: 20upx;
	}

	.cart_list .checkbox-box {
		padding-left: 16upx;
		flex-shrink: 0;
		height: 22vw;
		margin-right: 16upx;
		align-items: center;
		position: relative;
		width: 50upx;
	}

	.cart_list .checkbox-box .checkbox {
		width: 28upx;
		height: 28upx;
		border-radius: 100%;
		border: solid 1upx;
		position: absolute;
		top: 50%;
		left: 20upx;
		display: flex;
		align-items: center;
		justify-content: center;
		transform: translateY(-50%);
	}

	.cart_list .checkbox-box .checkbox .on {
		width: 20upx;
		height: 20upx;
		border-radius: 100%;
		align-items: center;
	}

	.cart_list .cover {
		width: 22vw;
		height: 22vw;
		flex-shrink: 0;
		margin-left: 10upx;
		border-radius: 8upx;
		overflow: hidden;
		margin-right: 20upx;
	}

	.cart_list .cover image {
		width: 100%;
		height: 100%;
		display: block;
	}

	.daodi {
		text-align: center;
		font-size: 24upx;
		color: #ccc;
		margin-top: 30upx;
	}

	.right {
		height: 22vw;
		width: 100%;
		display: flex;
		flex-wrap: wrap;
		padding-right: 15upx;
		position: relative;
	}

	.right .goods_name {
		width: 100%;
		font-size: 24upx;
		line-height: 34upx;
		max-height: 68upx;
		color: #333;
		overflow: hidden;
		font-weight: bold;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
	}

	.sku {
		font-size: 22upx;
		height: 50upx;
		line-height: 50upx;
		color: #a7a7a7;
		margin-bottom: 40upx;
	}

	.numbers {
		position: absolute;
		width: 100%;
		overflow: hidden;
		display: flex;
		justify-content: space-between;
		align-items: flex-end;
		height: 50upx;
		bottom: -5upx;
	}

	.numbers .price {
		font-size: 30upx;
		line-height: 50upx;
		color: red;
	}

	.numbers .right_btn {
		display: flex;
		justify-content: center;
		align-items: flex-end;
		margin-right: 20upx;
	}

	.right_btn .sub {
		width: 40upx;
		height: 40upx;
		font-size: 40upx;
		background-color: #f3f3f3;
		border-radius: 4upx;
		text-align: center;
		line-height: 40upx;
	}

	.right_btn .sub:active {
		background-color: #f8f8f8;
	}

	.right_btn .input {
		width: 50upx;
		height: 50upx;
		margin: 0 8upx;
		background-color: #f3f3f3;
	}

	.right_btn .input input {
		width: 50upx;
		height: 50upx;
		display: flex;
		font-size: 22upx;
		text-align: center;
		align-items: center;
		justify-content: center;
	}

	.right_btn .add {
		width: 40upx;
		height: 40upx;
		font-size: 40upx;
		background-color: #f3f3f3;
		border-radius: 4upx;
		text-align: center;
		line-height: 40upx;
	}

	.right_btn .add:active {
		background-color: #f8f8f8;
	}

	.del_mask {
		width: 100%;
		height: 100%;
		position: fixed;
		top: 0;
		left: 0;
		z-index: -21;
	}

	.dask_del {
		width: 100%;
		height: 100%;
		position: absolute;
		top: 0;
		left: 0;
		background-color: rgba(0, 0, 0, .5);
		z-index: -20;
		display: flex;
		align-items: center;
		opacity: 0;
		transition: all 0.3s;
	}

	.dask_del .del,
	.cencal {
		width: 100upx;
		height: 100upx;
		display: flex;
		justify-content: center;
		line-height: 100upx;
		border-radius: 100%;
		text-align: center;
		font-size: 24upx;
		background: linear-gradient(#FF5D39, #FFAF48);
		color: #ffffff;
		font-weight: 500;
		margin: 0 auto;
	}

	.dask_del text:active {
		opacity: 0.9;
	}

	.cencal {
		background: linear-gradient(#FFE846, #FFCD43);
	}
</style>
