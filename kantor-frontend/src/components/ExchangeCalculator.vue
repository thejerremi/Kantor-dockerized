<template>
  <v-card class="exchange">
        <v-tabs fixed-tabs>
          <v-tab text="Kup" @click="exchangeBuying = true"></v-tab>

          <v-tab text="Sprzedaj" @click="exchangeBuying = false"></v-tab>
        </v-tabs>
        <div v-if="exchangeBuying">
          <v-row class="mx-6 mt-2">
            <v-col cols="1">
              <v-select v-model="PLN" label="Waluta" readonly disabled></v-select>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="leftValue" label="Kwota"></v-text-field>
            </v-col>
            <v-col cols="2">
              <div class="text-h4 font-weight-bold text-center d-flex pt-3 justify-center align-center">=</div>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="rightValue" label="Kwota"></v-text-field>
            </v-col>
            <v-col cols="1">
              <v-select v-model="selectedCurrency" :items="currencyOptions" label="Waluta"></v-select>
            </v-col>
          </v-row>
        </div>
        <div v-else>
          <v-row class="mx-6 mt-2">
            <v-col cols="1">
              <v-select v-model="selectedCurrency" :items="currencyOptions" label="Waluta"></v-select>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="rightValue" label="Kwota"></v-text-field>
            </v-col>
            <v-col cols="2">
              <div class="text-h4 font-weight-bold text-center d-flex pt-3 justify-center align-center">=</div>
            </v-col>
            <v-col cols="4">
              <v-text-field v-model="leftValue" label="Kwota"></v-text-field>
            </v-col>
            <v-col cols="1">
              <v-select v-model="PLN" label="Waluta" readonly disabled></v-select>
            </v-col>
          </v-row>
        </div>
        <div class="text-h3 text-center" v-if="exchangeBuying">Za {{ leftValue }} PLN otrzymasz {{ rightValue }} {{ selectedCurrency }}</div>
        <div class="text-h3 text-center" v-if="!exchangeBuying">Za {{ rightValue }} {{ selectedCurrency }} otrzymasz {{ leftValue }} PLN</div>
      </v-card>
</template>

<script setup>
import { ref, onBeforeMount, computed, watch, nextTick } from 'vue';
import { useStore } from 'vuex';
const exchangeBuying = ref(true)
const store = useStore();
const selectedCurrency = ref('USD');
const currencyOptions = ref([]);
const leftValue = ref(0);
const rightValue = ref(0);
const currencyRates = computed(() => store.state.currencyRates);
const PLN = ref('PLN')

let updating = false;
onBeforeMount(async () => {
  setTimeout(() => {
    currencyOptions.value = currencyRates.value.map(rate => rate.code)
  }, 100)
});
watch (exchangeBuying, () => {
    leftValue.value = 0;
    rightValue.value = 0;
})
watch(selectedCurrency, () => {
  if (selectedCurrency.value) {
    const rate = currencyRates.value.find(rate => rate.code === selectedCurrency.value);
    if (rate) {
      rightValue.value = (leftValue.value / rate.ask).toFixed(2);
    }
  }
});
watch(leftValue, async (newValue) => {
  if (!updating && selectedCurrency.value) {
    updating = true;
    const rate = currencyRates.value.find(rate => rate.code === selectedCurrency.value);
    if (rate) {
      if(exchangeBuying.value)
        rightValue.value = (newValue / rate.ask).toFixed(2);
      else
        rightValue.value = (newValue * rate.bid).toFixed(2);
      await nextTick();
      updating = false;
    }
  }
});

watch(rightValue, async (newValue) => {
  if (!updating && selectedCurrency.value) {
    updating = true;
    const rate = currencyRates.value.find(rate => rate.code === selectedCurrency.value);
    if (rate) {
      if(exchangeBuying.value)
        leftValue.value = (newValue * rate.ask).toFixed(2);
      else
        leftValue.value = (newValue * rate.bid).toFixed(2);
      await nextTick();
      updating = false;
    }
  }
});
</script>

<style>
.exchange {
  height: 25vh;
}

</style>