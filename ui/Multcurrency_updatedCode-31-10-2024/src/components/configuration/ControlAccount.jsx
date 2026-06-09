import amsApi from "../../api/amsApi";
import AppLayout from "../../layout/AppLayout";
import React, { useState, useEffect } from "react";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
export default function ControlAccount() {
  const [alldata, setAlldata] = useState([]);
  const [categorydata, setCategorydata] = useState("");
  const splitData = categorydata.split("-");
  let participantID = sessionStorage.getItem("Participantid");
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

  const handleClick = () => {
    setDescription("");
    setType("");
    setCategorydata("");
  };

  // Dropdown select  Catogary list
  useEffect(() => {
    category();
  }, [categorydata]);

  const category = async () => {
    try {
      const response = await amsApi.post(`category_type/categoryTypedata`, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.data.code === "S0000") {
        setAlldata(response.data.category);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // save Category Type api
  const [description, setDescription] = useState("");
  const [type, setType] = useState("");
  // demo

  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      description === "" ||
      description.length === 0 ||
      type === "" ||
      type.length === 0 ||
      categorydata === ""
    ) {
      setError(true);
    } else if (!participantID) {
      swal("Please check your participantID ");
    } else {
      try {
        const response = await amsApi.post(
          `category_type/categoryTypeExit`,
          {
            strType: type,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code !== "S0000") {
          const response1 = await amsApi.post(`category_type/saveCategoryTyp`, {
            strParticipantID: participantID,
            strType: type,
            strDescription: description,
            strCategoryType: splitData[0].trim(),
          });
          if (response1.data.code === "S0000") {
            handleShowSuccess(response1.data.message);
            setError("");
            handleClick();
          } else {
            handleShowError(response1.data.message);
          }
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
      }
    }
  };

  return (
    <AppLayout>
      <div className="min-h-full first-line:flex items-center justify-center ">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xm  leading-normal ">
                Account Category
              </p>
            </div>
          </div>
        </div>
        <div className="  ">
          <form className="" onSubmit={handleSubmit}>
            <div className="overflow-hidden shadow ">
              <div className="px-4 sm:p-2 bg-white">
                <div className="grid gap-2  lg:grid-cols-1 md:grid-cols-1 px-4">
                  <div className="flex ">
                    <label
                      htmlFor="text"
                      className=" w-auto px-3 mr-1 py-1 text-base font-sans text-gray-700 bg-white "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Account Type
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="type"
                      maxLength={3}
                      minLength={1}
                      value={type}
                      onChange={(e) => {
                        const value = e.target.value.toUpperCase();
                        setType(value);

                        const regex = /^[a-zA-Z]+$/;
                        if (!regex.test(value)) {
                          setErrorMessage("Input can't be numeric");
                        } else {
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (type.length === 0) {
                          setError("Please Enter Account Type");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-60 px-3 py-1 text-sm font-sans text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Account Type"
                      autoComplete="off"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-sm font-medium">
                        {errorMessage}
                      </p>
                    ) : error && type.length <= 0 ? (
                      <p className="text-red-500 text-sm font-medium">
                        Please Enter Account Type!
                      </p>
                    ) : null}
                  </div>
                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3 mr-5 py-1 text-base font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700 "
                    >
                      Description
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="description"
                      name="description"
                      value={description}
                      disabled={!type}
                      onChange={(e) => setDescription(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Description"
                      autoComplete="off"
                    />
                    {error && description.length <= 0 ? (
                      <p className="text-red-500   text-sm font-medium">
                        Please enter Description
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div className="flex">
                    <label
                      htmlFor="text"
                      className="block w-auto px-3  py-1 text-base font-normal text-gray-700 bg-white  "
                      // className="block text-xs font-semibold text-gray-700"
                    >
                      Select Category{""}
                      <span className="text-red-600 px-1">*</span>
                    </label>
                    <select
                      name="Category"
                      id="Category"
                      disabled={!description}
                      value={categorydata}
                      onChange={(e) => setCategorydata(e.target.value)}
                      className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata.map((data) => (
                        <option
                          className="capatlize text-base"
                          // value={}
                        >
                          {data.strType || ""} - {data.strDescription || ""}
                        </option>
                      ))}
                    </select>
                    {error && categorydata.length <= 0 ? (
                      <p className="text-red-500 text-sm font-normal">
                        Please select category
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>
              </div>
              <div className="bg-gray-100 px-4 py-1 text-right sm:px-2 ">
                <button
                  title="Click Submit Button"
                  type="submit"
                  data-modal-toggle="defaultModal"
                  className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1 px-3  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                >
                  Submit
                </button>
                <button
                  title="Clear Data"
                  type="button"
                  onClick={handleClick}
                  className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1 px-3 mx-4 text-sm font-medium text-white shadow-sm hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
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
