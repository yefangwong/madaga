<script setup>
import VueWriter from 'vue-writer'
</script>

<template>
  <div class="container-fluid">
    <div class="row">
        <div class="col-md-12">
          <header>
            <h1>自然語言轉 SQL</h1>
          </header>
        </div>
    </div>
    <div class="row">
        <div class="col-md-8 offset-md-3">
          <main>
              <!-- 自然語言查詢表單開始 -->
              <form ref="form" role="form" @submit.prevent="doCompile">
                <div class="input-group mb-3">
                  <input v-model="sourceString" type="text" class="form-control-lg" size="40" placeholder="請輸入要轉換的查詢語句">
                  <select class="form-selet" >
                    <option value="0" selected>選擇算法</option>
                    <option value="1">Open AI</option>
                    <option value="2">SQLNet</option>
                    <option value="3">NLP2SQLCompiler</option>
                  </select>
                  &nbsp;
                  <button class="btn execute">執行</button>
                </div>
                <div class="input-group mb-3">
                  <VueWriter
                    :array="arr" 
                    :iterations='1' 
                    :typeSpeed="70"
                    :style="{visibility: status}"
                    :key="componentKey" />
                </div>
              </form>
              <!-- 自然語言查詢表單結束 -->
          </main>
        </div>
    </div>
  </div>
</template>
<style lang="scss">
$secondary: rgb(14, 36, 240);
.execute {
   background: $secondary!important;
   &:hover {
     color: white!important;
     background: darken($secondary, 5%)!important;
   }
}
</style>

<script>
export default {
  data() {
    return {
      arr: [""],
      status: "hidden",
      componentKey: 0,
      sourceString: "",
      targetString: ""
    }
  },
  methods: {
    reRender() {
      this.componentKey++;
    },
    doSubmit() {
      console.log("doSubmit:");
      this.$refs.form.submit();
    },
    doCompile() {
      // 描述要查詢的自然語言語句
      const nlSource = this.sourceString;
      console.log("doCompile:", nlSource);
      // 執行編譯轉換
      this.targetString = "SELECT COUNT(*) FROM Order";
      this.arr = [this.targetString];
      this.status = 'visible';
      this.reRender();
    }
  }
}
</script>

