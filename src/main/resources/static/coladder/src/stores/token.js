//定义token
import { defineStore } from "pinia";
import { ref } from "vue";

/**
 * 需要一个唯一的名字
 * 需要一个函数，在内部定义状态所有内容
 * 
 * 返回一个函数
 */
export const useTokenStore = defineStore('token',()=>{
    //1.响应变量
    const token = ref('');

    //定义函数修改函数值
    const setToken = (newToken)=>{
        token.value = newToken;
    }

    const removeToken = ()=>{
        token.value='';
    }

    return{
        token,setToken,removeToken
    }

},
{
    //持久化token的存储
    persist:true
});


