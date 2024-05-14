<template>
  <div>
    <v-carousel height="300" show-arrows="hover" progress="black" cycle hide-delimiters>
        <v-carousel-item class="bg-white" v-for="(rate, i) in currencyRates" :key="i">

          <v-container fluid>
            <v-row>
              <v-col cols="6" class="px-4 py-2" style="border-right: 1px solid #ccc;">
                <div class="text-h3 text-weight-bold text-center pb-3">
                  KUP
                </div>
                <div class="pt-10">
                <div class="text-h4 text-center">
                  {{ rate.code }}
                </div>
                <div class="text-h5 text-center">
                  1 {{ rate.code }} <- {{ rate.ask }} PLN
                </div>
                <div class="text-subtitle-3 font-italic text-center">
                  Za 1000 PLN otrzymasz {{ (1000 / rate.ask).toFixed(2) }} {{ rate.code }}
                </div>
              </div>
              </v-col>
              <v-col cols="6" class="px-4 py-2">
                <div class="text-h3 text-weight-bold text-center pb-3">
                  SPRZEDAJ
                </div>
                <div class="pt-10">
                  <div class="text-h4 text-center">
                    {{ rate.code }}
                  </div>
                  <div class="text-h5 text-center">
                    1 {{ rate.code }} -> {{ rate.bid }} PLN
                  </div>
                  <div class="text-subtitle-3 font-italic text-center">
                    Za 1000 {{ rate.code }} otrzymasz {{ (1000 * rate.bid).toFixed(2) }} PLN
                  </div>
                </div>
              </v-col>
            </v-row>
          </v-container>

        </v-carousel-item>
      </v-carousel>
  </div>
</template>

<script lang="ts" setup>
import { computed } from 'vue';
import { useStore } from 'vuex';
const store = useStore();
const currencyRates = computed(() => store.state.currencyRates);

</script>

<style>

</style>