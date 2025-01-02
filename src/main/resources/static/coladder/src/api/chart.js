import request from "@/utils/request.js"
import { ref } from "vue"


//回复消息获取
export const getChartService = () =>{
    //pinia中的响应式数据不需要.value 可以直接使用
    //return request.get('/model/getCharts',{headers : {'Authorization':tokenStore.token}})
    return request.get('/model/getCharts')
}

export const getAnswerService = (question) =>{
    const tem = ref({
        input : question
    })
    const params = new URLSearchParams();
    for(let key in tem.value){
        params.append(key,tem.value[key]);
    }
    return request.post('/model/call-model2',params)
}


export const picOrText = (code) => {
    const tem = ref({
        input : code
    })
    const params = new URLSearchParams();
    for(let key in tem.value){
        params.append(key,tem.value[key]);
    }
    return request.post('/model/getAnswer',params)
}