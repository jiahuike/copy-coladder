<script lang="ts" setup>
import type Node from 'element-plus/es/components/tree/src/model/node'
import type { DragEvents } from 'element-plus/es/components/tree/src/model/useDragNode'
import type {
    AllowDropType,
    NodeDropType,
} from 'element-plus/es/components/tree/src/tree.type'
import {
    Edit,
    Delete
} from '@element-plus/icons-vue'

import { ref,h,onMounted, onUnmounted,toRaw } from 'vue';
import { ElNotification } from 'element-plus';
import * as monaco from 'monaco-editor';
import { picOrText } from '@/api/chart';
import { RefSymbol } from '@vue/reactivity';
const showType = ref(true);
const pic = ref()
const data = ref({
    code: '',
    output: '',
    outputs: null,
    pyodide: null,
})
// 编辑器内容
const code = ref('')
const blockDataId = ref(13)
const blockData = ref([
    {
        nodeId: 1,
        label: 'Level one 1',
        children: [
        {
            nodeId : 2,
            label: 'Level two 1-1',
            children: [
            {
                nodeId : 3,
                label: 'Level three 1-1-1',
                code:"Level three 1-1-1\n4564654",
                children: []
            },
          ],
        },
      ],
    },
    {
        nodeId : 4,
      label: 'Level one 2',
      children: [
        {
            nodeId : 5,
          label: 'Level two 2-1',
          children: [
            {
              nodeId : 6,
              label: 'Level three 2-1-1',
              code:'Level three 2-1-1',
              children: []
            },
          ],
        },
        {
            nodeId : 7,
          label: 'Level two 2-2',
          children: [
            {
                nodeId : 8,
              label: 'Level three 2-2-1',
              code:'Level three 2-2-1\n4564654',
              children: []
            },
          ],
        },
      ],
    },
    {
        nodeId : 9,
      label: 'Level one 3',
      children: [
        {
            nodeId : 10,
          label: 'Level two 3-1',
          children: [
            {
                nodeId : 11,
              label: 'Level three 3-1-1',
              code:'Level three 3-1-1\n4564654',
              children: []
            },
          ],
        },
        {
            nodeId : 12,
          label: 'Level two 3-2',
          children: [
            {
                nodeId : 13,
              label: 'Level three 3-2-1',
              code:'Level three 3-2-1\n45\n64\n4\n5\n6\n4\n65\n4',
              children: []
            },
          ],
        },
      ],
    },
])



const handleDragStart = (node: Node, ev: DragEvents) => {
    console.log('drag start', node)
  }

  const handleDragEnter = (
    draggingNode: Node,
    dropNode: Node,
    ev: DragEvents
  ) => {
    console.log('tree drag enter:', dropNode.label)
  }

  const handleDragLeave = (
    draggingNode: Node,
    dropNode: Node,
    ev: DragEvents
  ) => {
    console.log('tree drag leave:', dropNode.label)
  }

  const handleDragOver = (draggingNode: Node, dropNode: Node, ev: DragEvents) => {
    console.log('tree drag over:', dropNode.label)
  }

  const handleDragEnd = (
    draggingNode: Node,
    dropNode: Node,
    dropType: NodeDropType,
    ev: DragEvents
  ) => {
    console.log('tree drag end:', dropNode && dropNode.label, dropType)
  }

  const handleDrop = (
    draggingNode: Node,
    dropNode: Node,
    dropType: NodeDropType,
    ev: DragEvents
  ) => {
    console.log('tree drop:', dropNode.label, dropType)
  }

  const allowDrop = (draggingNode: Node, dropNode: Node, type: AllowDropType) => {
    if (dropNode.data.label === 'Level two 3-1') {
      return type !== 'inner'
    } else {
      return true
    }
  }

  const allowDrag = (draggingNode: Node) => {
    
    return !draggingNode.data.label.includes('Level three 3-1-1')
  }
  
  //添加逻辑
  const addNode = (node : Node) => {
    const newNode = {
        nodeId : ++blockDataId.value,
          label: '新节点',
          code:"",
          children: []
      };
      console.log(node)
      console.log(blockData.value)
      node.data.children.push(newNode)
    
  }

  //删除代码块逻辑
  const deleteNode = (node : Node) => {
    console.log(node)
    console.log(blockData.value)
    if(removeNode(blockData.value,node.data.nodeId)){
      console.log("删除")
    }else{
      console.log("失败")
    }
  }
  const removeNode = (data : any,id : any) => {
    
    for (let i = 0; i < data.length; i++) {
        if (data[i].nodeId === id) {
          data.splice(i, 1);
          return true;
        }
        if (data[i].children) {
          const found = removeNode(data[i].children, id);
          if (found) {
            return true;
          }
        }
      }
      return false;
  }

  //复制代码块逻辑
  const copyNode = (node:Node) => {
    console.log(node)
    if(node.parent.id === 0){
      blockData.value.push(copyData(node))
    }else{
      node.parent.data.children.push(copyData(node))
    }
    console.log("复制")
    
  }

  const copyData = (node : Node) => {
    const newNode = {
          nodeId : ++blockDataId.value,
          label: node.data.label,
          code:node.data.code,
          children: []
    };
    for (let index = 0; index < node.childNodes.length; index++) {
      
      if(node.childNodes[index].childNodes){
        newNode.children.push(copyData(node.childNodes[index]))
      }else{
        newNode.children.push(node.childNodes[index])
      }
    }
    return newNode
  }

  //编辑按钮code 编辑区是否展开逻辑
  const editBox = ref([])
  const editNodeExpend = (node:Node) =>{
    if(editBox.value.findIndex(item => item === node.id) === -1){
        return false
    }else{
        return true
    }
  }
  const editNode = (node:Node) => {
    let index = editBox.value.findIndex(item => item === node.id)
    if(index === -1){
        editBox.value.push(node.id)
    } else{
        editBox.value.splice(index,1)
    }
    //editBox.value = !editBox.value
  }

//run 按钮逻辑
const runCode = (node:Node) =>{
  renewCode(node);
}

//更新响应式全局变量code
const renewCode = (node:Node) => {
  setBlockDataCode(blockData.value,node.data.nodeId,node.data.code);
  for (let index = 0; index < blockData.value.length;index++) {
    code.value += getBlockeDataCode(blockData.value[index])
    console.log(code.value)
  }
  
  // 不能直接使用editor 需要使用editor转化之后在使用setvalue和getvalue
  toRaw(editor.value).setValue(code.value)
}
const setBlockDataCode = (theData:any,nodeID: any,newCode :any) =>{
  for(let i = 0;i < theData.length;i++){
    if(theData.nodeId === nodeID){
      theData.code === newCode
    }else if(theData.children && theData.children.length > 0){
      for (let j= 0 ; j< theData.children.length ;j++) {
        setBlockDataCode(theData.children[j],nodeID,newCode)
      }
    }
  }
}
const getBlockeDataCode = (temData : any) =>{
  let theCode = ''
  
  if(!temData.children || temData.children.length === 0){
    theCode = theCode + temData.code + '\n';
  }else{
    for(let i = 0; i < temData.children.length;i++){
      theCode = theCode + getBlockeDataCode(temData.children[i]) + '\n'
    }
  }
  return theCode
}


  //控制组件对齐
  const getNodeWidth = (level :any) => {
      const baseWidth = 560; // 基础宽度
      const increment = 18; // 每个层级增加的宽度
      return `${baseWidth - (level - 1) * increment}px`; // 根据层级动态计算宽度
  }




//右侧组件

 // 编辑器容器div
 const editorContainer = ref(null);
// 编辑器实列
const editor = ref(null);


onMounted(() => {

   //if(editorContainer.value === null){
        editor.value = monaco.editor.create(editorContainer.value,{
            value:code.value,
            language:'python',
            theme: 'hc-light', // 编辑器主题 默认vs-dark VSCode 黑色主题,可选：vs、vs-dark、hc-black、hc-light
            readOnly: false, // 是否只读内容不可编辑
            //readOnlyMessage:{value:"不可以修改哦",supportThemeIcons:true,supportHtml:true}, // 为只读时编辑内日提示词
            codeLens: true, // 代码透镜
            folding: true, // 代码折叠
            snippetSuggestions: "bottom", // 代码提示
            tabCompletion: 'on', // 代码提示按tab完成
            foldingStrategy: 'auto', // 折叠策略
            smoothScrolling: true, // 滚动动画
        })
        
        editor.value.onDidChangeModelContent(() => {
          console.log('编辑器内容变更')
        })
        
    //}
})
onUnmounted(() => {
// 检查 editor 是否已初始化
    if (editor.value) {
        // 清理 editor 资源，避免内存泄漏
        console.log("清理成功")
        editor.value.dispose();
    }
});

//vue3问题导致不能直接只用getValue（）方法，获取monaco编辑器中的文本 
const getVal = () => {
	return toRaw(editor.value).getValue(); 
}

//初始化pyodide对象
const mounted1 = async() => {
    data.value.pyodide = await loadPyodide({
        //indexURL : "http://localhost:8080/pyodide/"
    });
}
mounted1()

const runPythonCodeText = async() => {
    console.log("text")
    if (!data.value.pyodide) {
        data.value.output = 'Pyodide not loaded';
    return;
    }
    data.value.code = getVal();
    
    try {
    
        await data.value.pyodide.loadPackage("matplotlib");
        //await data.value.pyodide.loadPackage("numpy");
        
        await data.value.pyodide.runPythonAsync('import sys\n' + 'from io import StringIO\n' + '# 创建 StringIO 对象来捕获输出\n' + 'output = StringIO()\n' + 'sys.stdout = output  # 重定向 stdout');
        
        await data.value.pyodide.runPythonAsync(data.value.code);
        const result = await data.value.pyodide.runPythonAsync('output.getvalue()')
        data.value.output = result
        await data.value.pyodide.runPythonAsync('sys.stdout = sys.__stdout__')  // 重置 stdout
    } catch (error) {
        data.value.output = `Error: ${error}`;
        console.log(error)
    }
    data.value.outputs = data.value.output.split('\n').map(line => h('div',line))
    //console.log(data.value.outputs)
    ElNotification({
        title:'result',
        //dangerouslyUseHTMLString:true,
        position:'bottom-right',
        duration:0,
        message:h('div',null,[data.value.outputs]),
    })
}

const runPythonCodePic = async() => {
    console.log("pic")
    if (!data.value.pyodide) {
        data.value.output = 'Pyodide not loaded';
        return;
    }
    data.value.code = 'import matplotlib\n' + 'matplotlib.use(' + "'Agg'" +')  # 使用'+ 'Agg' +'后端以便在没有显示设备的情况下生成图形\n' 
    + 'import base64\n' + 'from io import BytesIO\n' + getVal() + '\nbuf = BytesIO()\n'+'plt.savefig(buf, format='+"'png'"+')\n'
     + 'buf.seek(0)\n' + '# 将图像转换为 Base64 编码\n' + 'img_data = base64.b64encode(buf.read()).decode('+"'utf-8'"+')\n' + 'img_data';
    //data.value.code = getVal();

    //console.log(data.value.code)
    try {
        await data.value.pyodide.loadPackage("matplotlib");
        //await data.value.pyodide.loadPackage("numpy");

        const result = await data.value.pyodide.runPythonAsync(data.value.code);
        pic.value = 'data:image/png;base64,' + result;
        
    } catch (error) {
        data.value.output = `Error: ${error}`;
        console.log(error)
    }
    //show.value = true;
    ElNotification({
        title:'result',
        dangerouslyUseHTMLString:true,
        position:'bottom-right',
        duration:0,
        message:h('div', { style: { textAlign: 'center' } }, [
        h('img', {
          src: pic.value,
          alt: 'result',
          style: { width: '100%', maxWidth: '200px' },
        }),
        h('div', ''),
        ]),
        
    })
}

const runPythonCode = async() => {
    let input = getVal()
    console.log(input)
    try {
      let result = await picOrText("这段代码的结果是图片还是文字,你只需要回答图片或文字。" + input)
      let tem = result.data
      if (tem.includes("文字")) {
          runPythonCodeText()
      }else if(tem.includes("图片")){
          runPythonCodePic()
      }
    } catch (error) {
      console.log(error)
    }
    
}

</script>


<template >
  <el-row style="display:flex ; height:100%; width:100%">
    <!--左侧展示-->
    <el-card style="width: 40%;height: 100%;">
      <el-scrollbar style="height: 750px; width: 100%;">
        <el-tree
        class="el-tree"
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        :data="blockData"
        draggable
        node-key="id"
        @node-drag-start="handleDragStart"
        @node-drag-enter="handleDragEnter"
        @node-drag-leave="handleDragLeave"
        @node-drag-over="handleDragOver"
        @node-drag-end="handleDragEnd"
        @node-drop="handleDrop"
        >
            <template #default="{ node }" style="margin-bottom: 20px;">
                <el-col @click.stop>
                  <el-card :style="{width : getNodeWidth(node.level)}">
                    <el-row >
                        <el-input 
                        class="my-tree-children" 
                        style="flex:1"
                        v-model="node.data.label" 
                        autosize 
                        type="textarea" 
                        placeholder="请输入"
                        
                            />
                        <div>
                            <el-button class="my-tree-children-button" @click="addNode(node)">
                            <img src="@/assets/add.png" class="img"/>
                            </el-button>
                            <el-button class="my-tree-children-button" @click="deleteNode(node)">
                            <img src="@/assets/delete.png" class="img"/>
                            </el-button>
                            <el-button class="my-tree-children-button" @click="copyNode(node)">
                            <img src="@/assets/copy.png" class="img"/>
                            </el-button>
                            <el-button class="my-tree-children-button" @click="editNode(node)">
                            <img src="@/assets/edit.png" class="img"/>
                            </el-button>
                            <el-button class="my-tree-children-button" @click="runCode(node)">
                            <img src="@/assets/run.png" class="img"/>
                            </el-button>
                        </div>
                        
                    </el-row>
                  </el-card>

                        
                  <el-card v-if="node.childNodes.length === 0 && editNodeExpend(node)" :style="{width : getNodeWidth(node.level)}">
                      <el-input v-model="node.data.code" autosize type="textarea" class="code-input" spellcheck="false"> </el-input>
                  </el-card>
                
                </el-col>
            </template>
        </el-tree>
      </el-scrollbar>
    </el-card>
        
    <!--右侧展示-->
    <div style="width:55%;height:100%;">
        <el-card style="padding:0;margin:0 ;border:0 ;height:20px" />
        <div ref="editorContainer" style="width: 100%; height: 90%;"></div>
        <el-card style="height: 7%; width:100%; margin-bottom:0">
            <el-button @click="runPythonCode()" >Run Python Code</el-button>
        </el-card>
    </div>

  </el-row>
</template>

<style lang="scss" >
.el-card{
    .el-card__body{
        padding:10px;
    }
}

.el-tree{
    width: 95%;
    min-height: 100%;
    padding: 0px;
    .el-tree-node__content{
        height: auto;
        width: 100%;
    }
    .my-tree-children{
      display: flex;
      height: auto;
      width: 85%;
      flex:1;

    }
    .el-button+.el-button{
          margin:0px;
    }
    .my-tree-children-button{
        margin: 0px;
        padding-left: 1px;
        padding-right: 1px;
        
        .img{
          height: 20px; 
          width:20px;
        }
    }
  
}


</style>

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

.code-input {
  font-family: 'Courier New', Courier, monospace; /* 使用等宽字体 */
  background-color: #f5f5f5; /* 设置背景色 */
  border: 1px solid #ddd; /* 自定义边框 */
  border-radius: 4px; /* 圆角边框 */
  white-space: pre; /* 保持空格 */
  overflow: auto; /* 超出内容时显示滚动条 */
  
}
</style>