<script setup>
import { ref } from 'vue'
import axios from 'axios'
import apiConfig from '@/config/apiConfig'

const sourceString = ref('')
const targetString = ref('')
const status = ref('hidden')
const modelType = ref('1') // 1: OpenAI, 2: SQLNet
const isLoading = ref(false)

const executeCompile = async () => {
  if (!sourceString.value.trim()) {
    targetString.value = "請輸入有效的自然語言語句。"
    status.value = 'visible'
    return
  }

  isLoading.value = true
  status.value = 'hidden'
  targetString.value = ''

  try {
    const { apiPathMap } = apiConfig
    // Model selection is passed to backend via the payload instead of handling everything in frontend
    const res = await axios.post(apiPathMap.nl2sql, { 
      prompt: sourceString.value,
      modelType: parseInt(modelType.value, 10) 
    })
    
    if (res.data && res.data.result) {
      targetString.value = res.data.result
      status.value = 'visible'
    } else {
      targetString.value = '伺服器未回傳 SQL 結果。'
      status.value = 'visible'
    }
  } catch (error) {
    console.error("生成 SQL 時發生錯誤:", error)
    targetString.value = '系統發生異常，無法生成 SQL。'
    status.value = 'visible'
  } finally {
    isLoading.value = false
  }
}
</script>

<template>
  <div class="c-container" style="max-width: 800px; margin: 40px auto;">
    <h1 class="c-heading-1" style="text-align: center; margin-bottom: 32px;">自然語言轉 SQL 智能助理</h1>
    
    <div class="c-card" style="padding: 24px;">
      <form @submit.prevent="executeCompile" class="c-form">
        <div class="c-form-group" style="margin-bottom: 24px;">
          <label class="c-label" for="nl-input">自然語言查詢</label>
          <input 
            id="nl-input"
            v-model="sourceString" 
            type="text" 
            class="c-input" 
            style="width: 100%; padding: 12px; font-size: 16px;" 
            placeholder="請輸入要轉換的查詢語句，例如：查詢所有薪水大於五萬的員工"
          >
        </div>

        <div class="c-form-group" style="margin-bottom: 24px; display: flex; gap: 16px; align-items: center;">
          <select v-model="modelType" class="c-select" style="min-width: 200px;">
            <option value="0" disabled>選擇模型</option>
            <option value="1">OpenAI (推薦)</option>
            <option value="2">SQLNet (地端)</option>
            <option value="3">NLP2SQLCompiler (規則)</option>
          </select>

          <button 
            type="submit" 
            class="c-btn c-btn-primary" 
            :disabled="isLoading"
            style="flex: 1; padding: 12px 24px;"
          >
            {{ isLoading ? '生成中...' : '開始轉換' }}
          </button>
        </div>
      </form>
    </div>

    <div v-if="status === 'visible'" class="c-card" style="margin-top: 24px; padding: 24px; background-color: var(--c-surface-variant);">
      <h3 class="c-heading-3" style="margin-top: 0;">生成結果</h3>
      <pre style="background: #1e1e1e; color: #d4d4d4; padding: 16px; border-radius: 8px; overflow-x: auto; font-family: monospace; white-space: pre-wrap;"><code>{{ targetString }}</code></pre>
    </div>
  </div>
</template>

<style>
/* Use CorneliusUI variables if needed */
</style>
