import { ACCESS_TOKEN } from "../../constants";
import Navbar from "../components/Navbar";
import React from "react";
import RateTableUser from "./components/RateTableUser";



function RatePage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <Navbar/>
        <RateTableUser/>
      </div>
    );
  }
  
  export default RatePage;