import React from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { useState } from "react";
import Swal from "sweetalert2";
import CustomAlert from "../../layout/CustomAlert";

export default function AddChargerelated() {
  const [charge, setCharge] = useState("");
  const [description, setDescription] = useState("");
  const [error, setError] = useState("");

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };
  const showSuccess = (resMessage) => {
    Swal.fire({
      title: "Success",
      text: resMessage,
      allowOutsideClick: false,
      icon: "success",
      confirmButtonText: "OK",
    });
  };
  const showError = (resMessage) => {
    Swal.fire({
      text: resMessage,
      allowOutsideClick: false,
      icon: "error",
      title: "Oops...",
      confirmButtonText: "OK",
    });
  };

  const handleClick = () => {
    setCharge("");
    setDescription("");
  };
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      description === "" ||
      description.length === 0 ||
      charge === "" ||
      charge.length === 0
    ) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else {
      try {
        const response = await amsApi.post(
          `chargeRelatedMaster/validateChargeRelted`,
          {
            strChargeRelated: charge,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code !== "S0000") {
          const response1 = await amsApi.post(
            `chargeRelatedMaster/addChargeRelted`,
            {
              strChargeRelated: charge,
              strChargeRelatedDescription: description,
            }
          );
          if (response1.data.code === "S0000") {
            handleShowSuccess(response1.data.message);
            setError("");
            setCharge("");
            setDescription("");
          } else {
            handleShowError(response1.data.message);
          }
        } else {
          handleShowError("Duplicate Charge Related!!");
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10  bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Add Charge Related Config
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1  ">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow ">
              <div className=" px-2  sm:p-2 bg-white  ">
                <div className="grid gap-2  md:grid-cols-1 px-4">
                  <div className="flex">
                    <label
                      htmlFor="GL Account Type"
                      className="block w-auto px-3 py-1 mr-20 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Charge Related{""}
                      <span className="text-red-600 ">*</span>
                    </label>
                    <input
                      title="A to Z"
                      type="text"
                      id="Charge Related"
                      value={charge}
                      maxLength={1}
                      minLength={1}
                      onChange={(e) => setCharge(e.target.value.toUpperCase())}
                      // onBlur={(e) => setCharge(e.target.value.toUpperCase())}
                      className="block w-60 px-3 py-1 sm:text-xs  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Charge Related"
                      autoComplete="off"
                    />
                    {error && charge.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Related!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="flex">
                    <label
                      htmlFor="GL Account Description"
                      className="block w-auto px-3 py-1 mr-2 text-sm font-normal text-gray-700 bg-white  "
                    >
                      Charge Related Description{""}
                      <span className="text-red-600 ">*</span>
                    </label>
                    <input
                      title="Description"
                      type="text"
                      id="DefineCreditLimit"
                      value={description}
                      onChange={(e) => setDescription(e.target.value)}
                      className="block w-60 px-3 py-1 sm:text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Charge Related Description"
                      autoComplete="off"
                    />
                    {error && description.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Charge Desc!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3 mx-2  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>

                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-3 mx-2 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                >
                  Clear
                </button>
              </div>
            </div>
          </form>
        </div>
      </div>

      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
