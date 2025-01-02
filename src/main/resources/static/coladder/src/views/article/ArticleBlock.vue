<script lang="ts" setup>
import { ref } from 'vue'
import type Node from 'element-plus/es/components/tree/src/model/node'
import type { DragEvents } from 'element-plus/es/components/tree/src/model/useDragNode'
import type {
    AllowDropType,
    NodeDropType,
} from 'element-plus/es/components/tree/src/tree.type'

const data = ref([
    {
      label: 'Level one 1',
      children: [
        {
          label: 'Level two 1-1',
          children: [
            {
              label: 'Level three 1-1-1',
              code:"4564564\n4564654",
              children: []
            },
          ],
        },
      ],
    },
    {
      label: 'Level one 2',
      children: [
        {
          label: 'Level two 2-1',
          children: [
            {
              label: 'Level three 2-1-1',
              code:'',
              children: []
            },
          ],
        },
        {
          label: 'Level two 2-2',
          children: [
            {
              label: 'Level three 2-2-1',
              code:'4564564\n4564654',
              children: []
            },
          ],
        },
      ],
    },
    {
      label: 'Level one 3',
      children: [
        {
          label: 'Level two 3-1',
          children: [
            {
              label: 'Level three 3-1-1',
              code:'4564564\n4564654',
              children: []
            },
          ],
        },
        {
          label: 'Level two 3-2',
          children: [
            {
              label: 'Level three 3-2-1',
              code:'456\n45\n64\n4\n5\n6\n4\n65\n4',
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
  
  const addNode = (node : Node) => {
    const newNode = {
          label: '新节点',
          code:"",
          children: []
      };
      console.log(node)
      node.data.children.push(newNode)
    console.log(data)
  }
  const deleteNode = (node : Node) => {
    console.log(node)
    if(removeNode(node.parent,node.id)){
      console.log("删除")
    }else{
      console.log("失败")
    }
  }
  const removeNode = (data : Node , id : any) => {
    for (let i = 0; i < data.childNodes.length; i++) {
        if (data.childNodes[i].id === id) {
          data.childNodes.splice(i, 1);
          return true;
        }
        if (data.childNodes[i].childNodes) {
          const found = removeNode(data.childNodes[i], id);
          if (found) {
            return true;
          }
        }
      }
      return false;
  }

  const copyNode = (node:Node) => {
    node.parent.data.push(copyData(node))
    console.log("复制")
  }

  const copyData = (node : Node) => {
    const newNode = {
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

  //控制组件对齐
  const getNodeWidth = (level :any) => {
      const baseWidth = 800; // 基础宽度
      const increment = 18; // 每个层级增加的宽度
      console.log(level)
      return `${baseWidth - (level - 1) * increment}px`; // 根据层级动态计算宽度
  }

</script>


<template >
    <el-card style="width: auto;min-height: 100%;">
      <el-scrollbar style="max-height: 100%; width: 100%;">
        <el-tree
        class="el-tree"
        :allow-drop="allowDrop"
        :allow-drag="allowDrag"
        :data="data"
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
              <el-col >
                <el-row :style="{width : getNodeWidth(node.level)}">
                  <el-input 
                  class="my-tree-children" 
                  style="flex:1"
                  v-model="node.data.label" 
                  autosize 
                  type="textarea" 
                  placeholder="请输入"
                  @click.stop 
                    />
                <el-button class="my-tree-children-button" @click="addNode(node)">
                  <img src="@/assets/add.png" class="img"/>
                </el-button>
                <el-button class="my-tree-children-button" @click="deleteNode(node)">
                  <img src="@/assets/delete.png" class="img"/>
                </el-button>
                <el-button class="my-tree-children-button" @click="copyNode(node)">
                  <img src="@/assets/copy.png" class="img"/>
                </el-button>
                </el-row>
                
                <el-card v-if="node.childNodes.length === 0" :style="{width : getNodeWidth(node.level)}">
                    <el-input v-model="node.data.code" autosize type="textarea"  > </el-input>
                </el-card>
                
              </el-col>
                   
            </template>
                
          </el-tree>
            
        
    </el-scrollbar>
</el-card>
    

</template>

<style lang="scss" >


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
    .page-container{
        
        width: 100%;
        height: 100%;
        box-sizing: border-box;
    }


</style>