import React from "react";
import Navbar from "../components/Navbar";
import TransferForm from "./components/TransferForm";
import {ACCESS_TOKEN} from "../../constants";




function TransferPage() {
  if (!localStorage.getItem(ACCESS_TOKEN)) {
    document.location.href = '/login'
  }

  return (
    <div>
      <Navbar />
      <TransferForm />
    </div>
      );
}

      export default TransferPage;