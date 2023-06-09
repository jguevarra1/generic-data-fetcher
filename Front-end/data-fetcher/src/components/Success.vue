<script>
export default {
  props: {
      configList: Array,
  },
}
</script>


<template>
  <!-- Display the Success widget -->

  <router-link to="/status/success" class="relative">
    <button type="button" class="w-[100%] h-[207px] bg-white rounded-xl text-2xl font-bold shadow-blue-500/50 shadow-md hover:-translate-y-3">
      <div class="flex justify-around">
        <p class="text-emerald-400">Total Successes <span class="block"> {{this.configList.filter(config => config.status === 'success').length}}</span></p>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-emerald-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M5 13l4 4L19 7" />
        </svg>
      </div>
    </button>
  </router-link>
</template>