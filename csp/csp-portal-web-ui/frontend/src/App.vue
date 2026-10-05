<script setup>
import Nl2SqlView from '@/views/Nl2SqlView.vue'

// Import CorneliusUI global styles
import '@/cornelius-ui/index.css'
import '@/cornelius-ui/tokens.css'
import '@/cornelius-ui/typography.css'
import '@/cornelius-ui/components/buttons.css'
import '@/cornelius-ui/components/forms.css'
import '@/cornelius-ui/components/cards.css'
import '@/cornelius-ui/components/grid.css'
</script>

<template>
  <main>
    <Nl2SqlView />
  </main>
</template>

<style>
/* Global resets or overrides if needed */
body {
  margin: 0;
  padding: 0;
  background-color: var(--c-surface);
  color: var(--c-on-surface);
  font-family: var(--c-font-family-base, sans-serif);
}
</style>
