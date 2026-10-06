import { createApp } from 'vue'
import App from './App.vue'

// Import icon libraries for Quasar
import '@quasar/extras/material-icons/material-icons.css'

// Import Quasar CSS (it will use our sassVariables via Vite)
import 'quasar/src/css/index.sass'

import { Quasar } from 'quasar'
import { registerCorneliusWebComponents, registerCorneliusVueComponents } from './cornelius-ui'

registerCorneliusWebComponents()

const app = createApp(App)

// 註冊 CorneliusUI 專屬的 Vue 包裝元件
registerCorneliusVueComponents(app)

app.use(Quasar, {
  plugins: {}, // import Quasar plugins here (e.g. Notify, Dialog) if needed
})

app.mount('#app')
