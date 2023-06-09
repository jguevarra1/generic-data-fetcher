<script>
export default {
  props: {
      configList: Array,
  },
}
</script>

<template>
  <!-- Display the Error widget -->
  <router-link to="/status/error">
    <button type="button" class="w-[100%] h-[207px] text-2xl font-bold text-black bg-white rounded-xl hover:bg-red-300 shadow-blue-500/50 shadow-md hover:bg-white hover:-translate-y-3 ">
      <div class="flex justify-around">
        <p class="text-red-400">Total Errors <span class="block"> {{this.configList.filter(config => config.status === 'error').length}}</span></p>
        <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-red-400" fill="none" viewBox="0 0 24 24" stroke="currentColor" stroke-width="2">
          <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
        </svg>
      </div>
    </button>
  </router-link>
</template>