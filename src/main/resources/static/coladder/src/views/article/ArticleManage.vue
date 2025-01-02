<script setup>
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'
import { ref } from 'vue';
const showType = ref(true);
const pic = ref()
const data = ref({
    code: 'print("Hello from Pyodide!")',
    output: '',
    outputs: null,
    pyodide: null,
})


// 编辑器内容
const code = ref('')



</script>


<template>    
    
</template>


<style lang="scss" scoped>

.message-dialog {
    pointer-events: auto;
}

.page-container {
    display: flex;
    height: 100%;
    width: 100%;
    //box-sizing: border-box;
    .code{
        display: flex;
        height: 90%;
        width: 98%;
    }
}
.page-input {
    //display: flex;
    width: 100%;
    height: 100%;
    //box-sizing: border-box;
    justify-content: center;   
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>