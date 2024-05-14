<template>
  <v-app>
    <HeaderComponent />
    <router-view />
    <v-snackbar v-if="snackbarVisible" v-model="snackbar">
      {{ snackbarMessage }}

      <template v-slot:actions>
        <v-btn color="white" variant="text">
          Zamknij
        </v-btn>
      </template>
    </v-snackbar>
  </v-app>
</template>

<script setup>
import { ref, computed, onBeforeMount, onMounted } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import HeaderComponent from '@/components/HeaderComponent.vue';
const store = useStore();
const router = useRouter();
const snackbar = ref(true);
const snackbarVisible = computed(() => store.state.snackbarVisible);
const snackbarMessage = computed(() => store.state.snackbarMessage);
onBeforeMount(async () => {
  await store.dispatch('fetchCurrencyRates')
});
onMounted(async () => {
  await store.dispatch('getUserByToken')
});

router.beforeEach((to, from, next) => {
  if (to.path === '/dashboard' && !store.state.user) {
    next('/');
  } else if (to.path === '/admin' && (!store.state.user || store.state.user.role !== 'ADMIN')) {
    next('/');
  }
  else {
    next();
  }
});
</script>
<style>
.v-app {
  height: 100vh;
}
</style>