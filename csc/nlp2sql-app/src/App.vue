<script setup>
import VueWriter from 'vue-writer'
import axios from 'axios'
import apiConfig from './config/apiConfig'
import { Configuration, OpenAIApi } from "openai"
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
                    <option value="0">選擇算法</option>
                    <option value="1" selected>OpenAI</option>
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
@import url('@/assets/my.scss');
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
    getPrompt() {
      return `Create a SQL request to ${this.sourceString}`;
    },
    async doCompile() {
      // 描述要查詢的自然語言語句
      const nlSource = this.sourceString;
      console.log("doCompile:", nlSource);
      const { apiKey } = apiConfig;
      const configuration = new Configuration({
        apiKey: apiKey,
      });
      const openai = new OpenAIApi(configuration);
      // 執行編譯轉換
      const response = await openai.createCompletion({
          model: "text-davinci-002",
          prompt: this.getPrompt(),
          temperature: 0.3,
          max_tokens: 60,
          top_p: 1.0,
          frequency_penalty: 0.0,
          presence_penalty: 0.0,
          stop: ["#", ";"],
        });
      console.log("prompt:", this.getPrompt());
      console.log("response:", response.data.choices[0].text);
      this.targetString = response.data.choices[0].text;
      this.arr = [this.targetString];
      this.status = 'visible';
      this.reRender();
    }
  }
}
</script>

