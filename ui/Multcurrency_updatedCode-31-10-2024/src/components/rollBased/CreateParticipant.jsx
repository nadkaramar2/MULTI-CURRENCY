import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
export default function CreateParticipaint() {
  const [alldata3, setalldata3] = useState([]);
  const [alldata5, setalldata5] = useState([]);
  const [alldata6, setalldata6] = useState([]);
  const [strParticipantName, setStrParticipantName] = useState("");
  const [strDescription, setStrDescription] = useState("");
  const [strAddress1, setstrAddress1] = useState("");
  const [strAddress2, setstrAddress2] = useState("");
  const [strAddress3, setstrAddress3] = useState("");
  const [strPincode, setstrPincode] = useState("");
  const [strContactPersonName, setstrContactPersonName] = useState("");
  const [strContactPersonNumber, setstrContactPersonNumber] = useState("");
  const [strParticipant, setstrParticipant] = useState("");
  const [strCountry, setstrCountry] = useState("");
  const [strCity, setstrCity] = useState("");
  const [strState, setstrState] = useState("");

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

  // COUNTRY
  useEffect(() => {
    country();
  }, [strCountry]);

  const country = async () => {
    try {
      const response = await amsApi.post(`address/getCountrylist`, {});

      if (response.data.code === "S0000") {
        setalldata3(response.data.countryList);
      } else {
      }
    } catch (error) {}
  };

  // STATE
  const state = async (data) => {
    try {
      const response = await amsApi.post(`address/getStatelist`, {
        strCountryID: data,
      });

      if (response.data.code === "S0000") {
        setalldata5(response.data.stateList);
      } else {
      }
    } catch (error) {}
  };

  // CITY
  const city = async (data) => {
    try {
      const response = await amsApi.post(`address/getCitylist`, {
        strStateID: data,
      });

      if (response.data.code === "S0000") {
        setalldata6(response.data.cityList);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(
        "Sorry, the server is under mentaines. Please try again later"
      );
    }
  };

  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [errorMessage1, setErrorMessage1] = useState("");
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      strParticipantName === "" ||
      strDescription === "" ||
      strAddress1 === "" ||
      strCity === "" ||
      strState === "" ||
      strPincode === "" ||
      strContactPersonName === "" ||
      strContactPersonNumber === ""
    ) {
      setError(true);
      // swal("please enter a valid Input (non-empty values).");
    } else {
      try {
        const response = await amsApi.post(`participant_master/add`, {
          strParticipantName: strParticipantName,
          strDescription: strDescription,
          strParticipant: strParticipant,
          strAddress1: strAddress1,
          strAddress2: strAddress2,
          strAddress3: strAddress3,
          strCity: strCity,
          strState: strState,
          strPincode: strPincode,
          strContactPersonName: strContactPersonName,
          strContactPersonNumber: strContactPersonNumber,
        });
        if (response.data.code === "S0000") {
          handleShowSuccess(response.data.message);
          setError("");
          handleClick();
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
      }
    }
  };

  const handleClick = () => {
    setStrParticipantName("");
    setStrDescription("");
    setstrAddress1("");
    setstrAddress2("");
    setstrAddress3("");
    setstrPincode("");
    setstrContactPersonName("");
    setstrContactPersonNumber("");
    setstrCountry("");
    setstrCity("");
    setstrState("");
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Create Participant
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1 bg-white">
          <form action="#">
            <div className="sm:overflow-hidden ">
              <div className="px-4 sm:p-3">
                <div className="grid md:grid-cols-3 md:gap-6 px-8">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Participant Name
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strParticipantName"
                      name="strParticipantName"
                      value={strParticipantName}
                      onChange={(e) =>
                        setStrParticipantName(e.target.value.toUpperCase())
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Participant Name"
                      autoComplete="off"
                    />
                    {error && strParticipantName.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter ParticipantID!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div className="">
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Description
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      htmlFor="strDescription"
                      id="strDescription"
                      name="strDescription"
                      value={strDescription}
                      onChange={(e) => setStrDescription(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Write Description"
                      autoComplete="off"
                    />
                    {error && strDescription.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter ParticipantName!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                </div>

                <div>
                  {""}
                  <h2 className="font-normal md:font-bold px-8 mt-1 py-1">
                    Address Details
                  </h2>
                </div>
                <div className="grid md:grid-cols-3 md:gap-6 px-8">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Address1
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strAddress1"
                      name="strAddress1"
                      value={strAddress1}
                      onChange={(e) => setstrAddress1(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Address1"
                      autoComplete="off"
                    />
                    {error && strAddress1.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter Address1!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Address2
                    </label>
                    <input
                      type="text"
                      id="strAddress2"
                      name="strAddress2"
                      value={strAddress2}
                      onChange={(e) => setstrAddress2(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Address2"
                      autoComplete="off"
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Address3
                    </label>
                    <input
                      type="text"
                      id="strAddress3"
                      name="strAddress3"
                      value={strAddress3}
                      onChange={(e) => setstrAddress3(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Address3"
                      autoComplete="off"
                    />
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Country
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      name="strCountry"
                      id="strCountry"
                      value={strCountry}
                      onChange={(e) =>
                        setstrCountry(e.target.value, state(e.target.value))
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata3.map((data) => (
                        <option
                          className="capatlize text-lg"
                          value={data.countryId}
                        >
                          {data.countryName}
                        </option>
                      ))}
                    </select>
                    {error && strCountry.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select Country
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      State
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="strState"
                      name="strState"
                      value={strState}
                      onChange={(e) =>
                        setstrState(e.target.value, city(e.target.value))
                      }
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata5.map((data) => (
                        <option
                          className="capatlize text-lg"
                          value={data.stateId}
                        >
                          {data.strState}
                        </option>
                      ))}
                    </select>
                    {error && strState.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select State
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      City
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="strCity"
                      name="strCity"
                      value={strCity}
                      onChange={(e) => setstrCity(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    >
                      <option value="">Select</option>
                      {alldata6.map((data) => (
                        <option
                          className="capatlize text-lg"
                          value={data.cityId}
                        >
                          {data.cityName}
                        </option>
                      ))}
                    </select>
                    {error && strCity.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Select City
                      </p>
                    ) : (
                      ""
                    )}
                  </div>

                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Pin Code
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strPincode"
                      value={strPincode}
                      maxLength={10}
                      // onChange={(e) => setstrPincode(e.target.value)}
                      onChange={(e) => {
                        const value = e.target.value;
                        setstrPincode(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage(" Only allow numeric digits");
                        } else {
                          setErrorMessage("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (strPincode.length === 0) {
                          setError("Please enter Credit Limit!");
                          setErrorMessage("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter pin code"
                      autoComplete="off"
                    />
                    {errorMessage ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage}
                      </p>
                    ) : error && strPincode.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter Pincode!
                      </p>
                    ) : null}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Contact Person Name
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strContactPersonName"
                      name="strContactPersonName"
                      autoComplete="off"
                      value={strContactPersonName}
                      onChange={(e) => setstrContactPersonName(e.target.value)}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Contact Person Name"
                    />
                    {error && strContactPersonName.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please Enter ContactPerson Name!
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700"
                    >
                      Contact Person Number
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <input
                      type="text"
                      id="strContactPersonNumber"
                      maxLength={10}
                      minLength={10}
                      name="strContactPersonNumber"
                      autoComplete="off"
                      value={strContactPersonNumber}
                      // onChange={(e) =>
                      //   setstrContactPersonNumber(e.target.value)
                      // }
                      onChange={(e) => {
                        const value = e.target.value;
                        setstrContactPersonNumber(value);

                        const regex = /^[0-9]*$/;
                        if (!regex.test(value)) {
                          setErrorMessage1(" Only allow numeric digits");
                        } else {
                          setErrorMessage1("");
                        }
                        setError("");
                      }}
                      onBlur={() => {
                        if (strPincode.length === 0) {
                          setError("Please Enter ContactPerson Number!");
                          setErrorMessage1("");
                        }
                      }}
                      className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      placeholder="Enter Contact Person Number"
                    />
                    {errorMessage1 ? (
                      <p className="text-red-500 text-xs font-medium">
                        {errorMessage1}
                      </p>
                    ) : error && strContactPersonNumber.length <= 0 ? (
                      <p className="text-red-500 text-xs font-medium">
                        Please Enter ContactPerson Number!
                      </p>
                    ) : null}
                  </div>
                </div>
              </div>
            </div>
            <div className="px-4 py-2 space-x-4 bg-gray-50 text-right sm:px-6">
              <button
                title="Click Submit Button"
                onClick={handleSubmit}
                type="submit"
                className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 text-white text-sm   py-1 px-2 border border-blue-700 rounded"
              >
                Submit
              </button>
              <button
                title="Clear Data"
                onClick={handleClick}
                type="danger"
                className="bg-red-500 hover:bg-red-700 focus:ring-2 focus:ring-red-800 focus:ring-offset-2 text-white  py-1 text-sm  px-4 mx-2 border border-red-700 rounded"
              >
                Clear
              </button>
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
