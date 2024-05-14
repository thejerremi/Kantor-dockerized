<template>
  <v-app-bar :elevation="8">
    <span class="mr-4"></span>
    <v-app-bar-title @click="router.push('/')">Kantor</v-app-bar-title>
    <v-btn class="ml-4" @click="router.push('/')">Strona główna</v-btn>
    <v-btn v-if="user" @click="router.push('/dashboard')">Panel użytkownika</v-btn>
    <v-btn v-if="user && user.role === 'ADMIN'" @click="router.push('/admin')">Panel administratora</v-btn>
    <v-spacer></v-spacer>
    <div v-if="!user">
      <v-btn text @click="goToRegister()">Zarejestruj się</v-btn>
      <v-btn text @click="goToLogin">Zaloguj się</v-btn>
    </div>
    <div v-else>
      <div class="appbar">
        <wallet-component />
        {{ user.email }}
        <v-btn text @click="logout()">Wyloguj</v-btn>
      </div>
    </div>
    
  </v-app-bar>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
import WalletComponent from '@/components/WalletComponent.vue';
const store = useStore();
const router = useRouter();
const user = computed(() => store.state.user);
const goToLogin = () => {
  router.push('/login');
}
const goToRegister = () => {
  router.push('/register');
}
const logout = () => {
  store.dispatch('logoutUser');
  router.push('/')
}
</script>

<style scoped>
.v-app-bar {
  background-color: #000000 !important;
}
.v-app-bar-title{
  cursor: pointer;
  display: contents;

}
.v-toolbar-title,
.v-btn,
.appbar {
  color: #ffffff !important;
}
</style>