<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue'
import { useTokenStore } from '@/stores/token';

const input = ref('')
const charts = ref([])
const answers = ref()

//异步函数
import { getChartService,getAnswerService } from '@/api/chart';
import { EventSourcePolyfill } from 'event-source-polyfill';

const tokenStore = useTokenStore();
const chartList = async() =>{
    let result = await getChartService();
    charts.value = result.data;
    
}

chartList();

/*
const eventsSource = function name(source) {
    let _this = this;
    this.source = new EventSourcePolyfill('/model/call-model',
        {
            headers :{
                'Authorization' : tokenStore.token
            },
            heartbeatTimeout: 10 * 60 * 1000
        }
    );
    this.source.onopen = () => {
      console.log("NOTICE建立连接");
    };
    this.source.onmessage = (e) => {
      _this.scrollMessage = e.data;
      console.log("NOTICE接收到消息");
    };
    this.source.onerror = (e) => {
      if (e.readyState == EventSource.CLOSED) {
        console.log("NOTICE连接关闭");
      } else if (this.source.readyState == EventSource.CONNECTING) {
        console.log("NOTICE正在重连");
        //重新设置header
        this.source.headers = {
          'Authorization' : tokenStore.token
        };
      } else {
        console.log(e);
      }
    };
}
*/

const questions = ref('')

const getAnswers = async(question) => {
    questions.value = input.value
    input.value = '';
    let result = await getAnswerService(question);
    //answers.value = result.data;
    //console.log(12312313213)
    chartList();
    questions.value = '';
    //console.log(result.data)
}


/*

const eventSource = new EventSource('/model/call-model' ,{
    headers :{
        'Authorization' : tokenStore.token
    }
});


eventSource.onmessage = function(event) {
    console.log("Received data: ", event.data);
    // 处理接收到的实时数据
    return event.data;
};

eventSource.onerror = function() {
    console.log("Error occurred");
    eventSource.close();
};*/




</script>


<template >
    <el-scrollbar class="page-container">
        <el-card >
            <el-scrollbar :style="{ width: '100%'}" v-for="(item, index) in charts" :key="index">
                <el-row class="el-row-right-chart">
                    <el-card class="chart-right">
                        <el-text class="right" :type="item.question.type">{{ item.question }}</el-text>
                    </el-card>
                </el-row>
                <br/>
                <el-row class="el-row-left-chart">
                    <el-card class="chart-left">
                        <el-message class="left" :type="item.answer.type">{{ item.answer }} </el-message>
                    </el-card>
                </el-row>
                
                
                
                <br/>
            </el-scrollbar>
            <el-message class="right" v-if="questions!==null">{{ questions }}</el-message>
        </el-card>
    </el-scrollbar>
    
    <el-card>
        <div class="page-input" style="margin: 0px 10px" >
            <el-input class="input" v-model="input" :rows="1" type="textarea" placeholder="请输入" />
            <div class="extra">
                <el-button type="primary" @click="getAnswers(input)"> 发送 </el-button>
            </div>
        </div>
    </el-card>


</template>

<style lang="scss" scoped>


.page-input {
    display: flex;
    width: 100%;
    max-height: 10%;
    //box-sizing: border-box;
    justify-content: center;
    .input{
        width: 90%;
    }   
}
.page-container {
    max-height: 90%;
    box-sizing: border-box;
    justify-content: center;
    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
    .el-row-right-chart{
        display: flex;
        max-width: 100%;
        justify-content: flex-end;
        .chart-right{
            max-width: 80%;
            display: flex;
            justify-content: flex-end;
        }
        .right{
            display:flex;
            justify-content: flex-end;
        }
    
    }
    
    .el-row-left-chart{
        display: flex;
        max-width: 100%;
        .chart-left{
            max-width: 80%;
            display: flex;
            justify-content: flex-start;
        }
        .left{
        display: flex;
        justify-content: left;
        }   
        
    }
    
    
}


</style>