<script>
export default {
  props: {
      configList: Array,
  },
}
</script>


<template>
  <!-- Display the Updating widget -->

  <router-link to="/status/updating">
    <button type="button" class=" w-[100%] h-[207px] text-2xl font-bold text-black bg-white rounded-xl shadow-blue-500/50 shadow-md hover:-translate-y-3">
      <div class="flex justify-around">
        <p class="text-yellow-400">Total Updating <span class="block"> {{this.configList.filter(config => config.status === 'updating').length}}</span></p>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-yellow-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
        </svg>
      </div>
    </button>
  </router-link>
</template>