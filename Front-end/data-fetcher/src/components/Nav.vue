<template>
    <!-- A navbar -->
    <nav class="relative bg-blue-600 px-2 py-2">
      <div class="container relative flex flex-wrap items-center justify-between">
        <router-link to="/" class="flex relative left-12 py-auto ml-12">
          <svg class="w-6 mx-3" fill="none" stroke="white"
            viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16"></path>
          </svg>
          <span
            class="text-white text-2xl font-bold whitespace-nowrap">
            Data Fetcher.
          </span>
        </router-link>
      </div>
    </nav>
</template>