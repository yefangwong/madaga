import { createApp } from 'vue'
import App from './App.vue'

// Import icon libraries for Quasar
import '@quasar/extras/material-icons/material-icons.css'

// Import Quasar CSS (it will use our sassVariables via Vite)
import 'quasar/src/css/index.sass'

import { Quasar } from 'quasar'

const app = createApp(App)

app.use(Quasar, {
  plugins: {}, // import Quasar plugins here (e.g. Notify, Dialog) if needed
})

app.mount('#app')
