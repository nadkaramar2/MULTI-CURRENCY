import React, { useState } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import Swal from "sweetalert2";
import CustomAlert from "../../layout/CustomAlert";
export default function CreateCategory() {
  const [type, settype] = useState("");
  const [description, setdescription] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

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

  // save form
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (type === "" || type.length === 0 || description === "") {
      setError(true);
    } else {
      try {
        const response = await amsApi.post(`category_type/saveCategoryTyp`, {
          strType: type,
          strDescription: description,
        });
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  const handleClick = () => {
    settype("");
    setdescription("");
  };
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base sm:text-xm text-black :text-2xl  leading-normal ">
                Add Account Category
              </p>
            </div>
          </div>
        </div>
        <div className="md:col-span-2 lg:col-span-1 bg-white">
          <form className="">
            <div className="overflow-hidden shadow ">
              <div className=" px-4  sm:p-2 bg-white  ">
                <div className="grid gap-3 mb-2 px-8  md:grid-cols-3 ">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Category Type<span className="text-red-600 px-2">*</span>
                    </label>

                    <input
                      type="text"
                      maxLength={1}
                      id="type"
                      value={type}
                      onChange={(e) => {
                        const value = e.target.value.toUpperCase();
                        settype(value);
                        const regex = /^[a-zA-Z]+$/;
                        if (!regex.test(value)) {
                          setErrorMessage("Input can't be Numeric");
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (type.length === 0) {
                          setError("Enter Category Type");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Type   "
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-sm font-medium">
                        {errorMessage}
                      </p>
                    ) : error && type.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please Enter Category Type
                      </p>
                    ) : null}
                  </div>

                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Description
                      <span className="text-red-600">*</span>
                    </label>
                    <input
                      type="text"
                      id="description"
                      value={description}
                      onChange={(e) => setdescription(e.target.value)}
                      className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter category Description"
                    />
                    {error && description.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please Enter Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4  text-right sm:px-6 ">
                <button
                  title="Click Submit Button"
                  type="button"
                  data-modal-toggle="defaultModal"
                  onClick={handleSubmit}
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-3 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
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
