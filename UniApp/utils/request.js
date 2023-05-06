/**
 * 封装的异步请求处理函数
 * 使用方法为:
 * request('接口名称',{key:value},'请求方式(默认为GET)')
 * .then(res=>{console.log(res)})
 */
import { getToken, removeToken } from "./auth";
let baseUrl = '';
async function request(mehtod, params, type, callBack) {
  //创建一个名为request请求的方法函数
  if (!type) {
    type = 'GET';
  }
  let header = {
    //设置请求头信息
    // 'Authorization': getToken(),
    'X-Requested-With': 'XMLHttpRequest',
    "Accept": "application/json",
    "Content-Type": "application/json; charset=UTF-8",
	// "token":uni.getStorageSync('token'),
	"token":"9d5feb6d-c032-4207-8b97-37e0bc9d8243"
  };
  let http = {
    url: mehtod,
    data: params,
    method: type,
    header: header
  };
  let promise = new Promise((resolve, reject) => {
    uni.request(http).then(res => {
      let newdata = res[1].data;

      if (newdata.code!=0) {
        //如果错误码为 -1 提示
        uni.showToast({
          title: newdata.msg,
          icon: 'none'
        });
      }
      resolve(res[1].data);
    }).catch(err => {
      reject(err);
      console.log(err);
    });
  });
  return promise;
}
export default {
  request
};