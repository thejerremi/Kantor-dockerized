<template>
  <v-main class="bg">
    <div class="text-h3 font-weight-bold mt-12 text-center">
      Najlepszy kantor polskiej waluty
    </div>
    <div class="text-subtitle-1 mt-6 text-center">
      Sprawdź ile otrzymasz za swoje pieniądze
    </div>
    <div class="pa-6">
      <ExchangeCalculator />
    </div>
    <div class="carousel" v-if="showCarousel">
      <RatesCarousel />
    </div>
    <div v-if="!user">
      <div class="text-h4 text-center">
      Podoba ci sie nasza oferta?
      <v-btn to="/register">Zarejestruj się</v-btn>
    </div>
    <div class="text-h5 text-center pt-6">
      Masz już konto?
      <v-btn to="/login">Zaloguj się</v-btn>
    </div>
    </div>
  </v-main>
</template>

<script setup>
import { ref, computed, onBeforeMount} from 'vue';
import { useStore } from 'vuex';
import { useRouter } from 'vue-router';
import RatesCarousel from '@/components/RatesCarousel.vue';
import ExchangeCalculator from '@/components/ExchangeCalculator.vue'
const store = useStore();
const router = useRouter();
const user = computed(() => store.state.user);
const showCarousel = ref(false);

onBeforeMount(() => {
  setTimeout(() => {
    showCarousel.value = true;
  }, 100)
});


</script>
<style scoped>
.bg {
  background-image: linear-gradient(120deg, #fdfbfb 0%, #ebedee 100%);
}

.carousel {
  position: fixed;
  bottom: 0;
  width: 100%;
}
</style>