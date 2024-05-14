<template>
    <v-main>
      <div class="auth-view">
        <v-container align="center">
          <v-card>
            <v-card-title>
              <v-icon>mdi-account-circle</v-icon>
              <span class="title">Rejestracja</span>
            </v-card-title>
            <v-card-text>
              <v-form @submit.prevent="handleRegistration" ref="form" v-model="valid">
                <v-text-field
                  v-model="user.firstName"
                  label="Imię"
                  outlined
                  dense
                  required
                ></v-text-field>
                <v-text-field
                  v-model="user.lastName"
                  label="Nazwisko"
                  outlined
                  dense
                  required
                ></v-text-field>
                <v-text-field
                  v-model="user.email"
                  label="Email"
                  outlined
                  dense
                  type="email"
                  :rules="emailRules"
                  required
                ></v-text-field>
                <v-text-field
                  v-model="user.password"
                  label="Hasło"
                  outlined
                  dense
                  type="password"
                  :rules="passwordRule"
                  required
                ></v-text-field>
                <v-text-field
                  v-model="user.confirmPassword"
                  label="Powtórz hasło"
                  outlined
                  dense
                  type="password"
                  :rules="passwordMatchRule"
                  required
                ></v-text-field>
                <v-btn
                  color="primary"
                  type="submit"
                  :disabled="!valid"
                >
                  Zarejestruj
                </v-btn>
              </v-form>
            </v-card-text>
          </v-card>
        </v-container>
      </div>
    </v-main>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router';
import { ref, reactive, onMounted } from 'vue';
  import { useStore } from 'vuex';
  onMounted (() => {
  if(store.state.user){
    router.push('/dashboard');
  }
});
  const router = useRouter();
  const store = useStore();
  const valid = ref(false);
  const user = reactive({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: ''
  });
  const emailRules = [
    v => !!v || 'Email jest wymagany',
    v => /.+@.+\..+/.test(v) || 'Email musi być prawidłowy'
  ];
  const passwordRule = [
    v => !!v || 'Hasło jest wymagane'
  ]
  const passwordMatchRule = [
    () => user.password === user.confirmPassword || 'Hasła nie są takie same',
    v => !!v || 'Powtórz hasło'
  ];
  
  async function handleRegistration() {
    if (valid.value) {
      const userFormatted = {
        firstname: user.firstName,
        lastname: user.lastName,
        email: user.email,
        password: user.password
      };
      await store.dispatch('registerUser', userFormatted);
      if(store.state.user)
        router.push('/dashboard');
    }
  }
  
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