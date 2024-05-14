<template>
  <v-main>
    <div class="auth-view">
      <v-container align="center">
        <v-card>
          <v-card-title>
            <v-icon>mdi-account-circle</v-icon>
            <span class="title">Logowanie</span>
          </v-card-title>
          <v-card-text>
            <v-form @submit.prevent="handleLogin" ref="form" v-model="valid">
              <v-text-field
                v-model="loginForm.email"
                label="Email"
                outlined
                dense
                required
                :rules="emailRules"
              ></v-text-field>
              <v-text-field
                v-model="loginForm.password"
                label="Hasło"
                outlined
                dense
                type="password"
                required
                :rules="passwordRule"
              ></v-text-field>
              <v-btn
                color="primary"
                type="submit"
                :disabled="!valid"
              >
                Zaloguj
              </v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-container>
    </div>
  </v-main>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
onMounted (() => {
  if(store.state.user){
    router.push('/dashboard');
  }
});
const router = useRouter();
const store = useStore();
const valid = ref(false);
const loginForm = ref({
  email: '',
  password: ''
});
const emailRules = [
    v => !!v || 'Email jest wymagany',
    v => /.+@.+\..+/.test(v) || 'Email musi być prawidłowy'
  ];
  const passwordRule = [
    v => !!v || 'Hasło jest wymagane'
  ]
const handleLogin = async () => {
  try {
     await store.dispatch('loginUser', loginForm.value);
     if(store.state.user)
    router.push('/dashboard');
  } catch (error) {
    console.error('Login error:', error);
  }
};
</script>

<style scoped>
.auth-view {
  height: 100vh;
  width: 100vw;
  background: linear-gradient(0deg, rgba(255,255,255,1) 0%, rgba(0,0,0,1) 100%);
}
.v-text-field {
  width: auto;
  max-width: 33%;
}
.v-container{
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}
</style>