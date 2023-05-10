import React, { useState, useEffect } from "react";
import axios from "axios";
import { ACCESS_TOKEN, API_BASE_URL } from "../../constants";

function Main() {
  const [data, setData] = useState({});

  axios.defaults.headers.common['Authorization'] = localStorage.getItem(ACCESS_TOKEN);

  useEffect(() => {
  axios.get(API_BASE_URL).then((resp) => {
      setData(resp.data);
    });
  }, []);
  
  return (
    <div>
    
    <section className ="mb-3">
      <div className="container mt-5">
        <div className="row">
          <div className="col-md-12 mx-auto">
            <div className="card">
              <div className="card-header">
                <h4>{data.title}</h4>
              </div>
              <div className="card-body">
                <p className="mt-3" >{data.body} </p>
                <div className="col-md-3  mt-4">
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    </div>
  );
}

export default Main;