import React from "react";
import Navbar from "../components/Navbar";
import {ACCESS_TOKEN} from "../../constants";
import CreditForm from "./components/CreditForm";




function CreditPage() {
  if (!localStorage.getItem(ACCESS_TOKEN)) {
    document.location.href = '/login'
  }

  return (
    <div>
      <Navbar />
      <CreditForm/>
    </div>
      );
}

      export default CreditPage;