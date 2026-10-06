<template>
  <!-- 
    核心門面模式 (Facade Pattern): 
    1. 呼叫 Quasar 的 q-input，將外部傳進來的所有屬性 ($attrs) 與插槽 ($slots) 
    轉發給底層的 q-input。
    2. 保留給未來可以更換其他 Input libarary 的空間。
    3. 抽換其他 UI libarary 的方式：
      1. 在 src/cornelius-ui/components/CorneliusInput.ce.vue 中，直接呼叫其他 UI libarary 的 Input，不使用 Quasar 的 q-input。
      2. 在 src/cornelius-ui/index.js 中，將 exportComponents 的 name 由 cornelius-input 修改為其他 name，並在
         src/views/Nl2SqlView.vue 中修改為 <cornelius-new-input>。
    4. 更換為其他前端框架的方式，例如 React，則是在 React 中使用 ref 取得 DOM 元素，直接呼叫原生 DOM 元素或相關 libarary 的 API。
  -->
  <q-input
    class="cornelius-input"
    v-bind="$attrs"
    :outlined="outlined"
    :color="color"
  >
    <!-- 透傳所有 Slots (例如 append, prepend 等圖示插槽) -->
    <template v-for="(_, name) in $slots" #[name]="slotData">
      <slot :name="name" v-bind="slotData || {}" />
    </template>
  </q-input>
</template>

<script setup>
defineProps({
  // 強制設定一些符合我們設計語言的預設值
  color: {
    type: String,
    default: 'primary'
  },
  outlined: {
    type: Boolean,
    default: true
  }
});
</script>

<style>
/* 
  =========================================
  CorneliusUI 專屬樣式區 
  (把 Quasar 的 Material Design 改造成 Apple HIG)
  =========================================
*/

/* 1. 蘋果柔和圓角 */
.cornelius-input .q-field__control {
  border-radius: var(--cui-radius-md, 14px) !important;
  transition: box-shadow 0.2s ease, border-color 0.2s ease;
}

/* 2. 統一字體，覆寫預設的 Roboto */
.cornelius-input .q-field__native,
.cornelius-input .q-field__label,
.cornelius-input .q-field__messages {
  font-family: var(--cui-font-base) !important;
}

/* 3. Focus 時的蘋果光影效果 (取代預設的粗邊框) */
.cornelius-input.q-field--focused .q-field__control {
  /* 讓邊框顏色維持稍微變亮，但加上高級的發光陰影 */
  box-shadow: 0 0 0 3px rgba(226, 109, 56, 0.15) !important;
}

/* 隱藏 Quasar 預設的 hover 變黑底色 */
.cornelius-input .q-field__control:before {
  border-color: rgba(0, 0, 0, 0.15) !important;
}
.cornelius-input:hover .q-field__control:before {
  border-color: rgba(0, 0, 0, 0.3) !important;
}
</style>
