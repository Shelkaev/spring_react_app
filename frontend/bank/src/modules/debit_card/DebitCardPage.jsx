import { ACCESS_TOKEN } from "../../constants";
import DebitCard from "./components/DebitCard";
import Navbar from "../components/Navbar";
import React from "react";



function DebitCardPage() {
  if(!localStorage.getItem(ACCESS_TOKEN)){
    document.location.href='/login'
  }

    return (
      <div>
        <Navbar/>
        <DebitCard/>
      </div>
    );
  }
  
  export default DebitCardPage;