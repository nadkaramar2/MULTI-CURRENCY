import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import CustomAlert from "../../layout/CustomAlert";
import swal from "sweetalert";
const NubanTypeConfig = () => {
  const [showhide, setShowhide] = useState("");
  localStorage.setItem("nubantype", showhide);
  // let Nubantype = localStorage.getItem("nubantype");
  const [typedata, setTypedata] = useState([]);

  const [code, setCode] = useState("");
  const [numberCode, setNumberCode] = useState("9");
  const [createview, setCreateView] = useState(true);
  const [desc, setDesc] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  // view
  const [data1, setData1] = useState({});
  let ntype = data1.strNubanType;
  let code1 = data1.strNubanCode;
  let nDescription = data1.strNubanDescription;
  let participantID = sessionStorage.getItem("Participantid");

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
  const handlesshowhide = (event) => {
    const getuser = event.target.value;
    setShowhide(getuser);
  };
  // Create form

  useEffect(() => {
    nubantype();
  }, []);

  const nubantype = async () => {
    try {
      const response = await amsApi.get(`nubanType/getType`, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.data.code === "S0000") {
        setTypedata(response.data.nubanTypes);

        // showSuccess(response.data.message);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error);
    }
  };

  // submit
  const handleSubmit = async (event) => {
    event.preventDefault();
    if (
      showhide === "" ||
      showhide.length === 0 ||
      code === "" ||
      desc === ""
    ) {
      setError(true);
    } else if (!participantID) {
      swal("Please check your participantID ");
    } else {
      try {
        const response1 = await amsApi.post(`nubanCode/add`, {
          strNubanType: showhide,
          strParticipantId: participantID,
          strNubanCode: numberCode + code,
        });
        if (response1.data.code === "S0000") {
          // setAlldata(response1.data);
          handleShowSuccess("Nuban type Created Successfully");
          setError("");
          setShowhide("");
          setDesc("");
          setCode("");
        } else {
          handleShowError("Nuban type Not Created ");
        }
      } catch (error) {
        handleShowError(error);
      }
    }
  };
  const handleClick = () => {
    setShowhide("");
    setDesc("");
    setCode("");
  };

  // View data

  useEffect(() => {
    view();
  }, {});
  const view = async () => {
    setCreateView(false);
    try {
      const response = await amsApi.post(
        `nubanCode/getNubanCode`,
        {
          strNubanType: showhide,
          strParticipantId: participantID,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setData1(response.data.nubanCodeConfigObject);
        setCreateView(true);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  return (
    <AppLayout>
      {/* Create Form */}
      {createview === false && (
        <div className="max-w-7xl mx-auto h-full">
          <div className="w-full shadow-md mt-2 bg-white">
            <div className="px-4 sm:px-10 rounded-t-lg ">
              <div className=" flex  items-center justify-between">
                <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                  Nuban Type Configuration
                </p>
              </div>
            </div>
          </div>
          <div className=" md:col-span-2 lg:col-span-1 sm:rounded-md bg-white">
            <form className="" onSubmit={handleSubmit}>
              <div className="overflow-hidden shadow sm:rounded-md">
                <div className=" px-4  sm:p-2 bg-white">
                  <div className="grid gap-4  md:grid-cols-1 px-8 ">
                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3 py-1  text-base font-normal text-gray-700 bg-white  "
                      >
                        Nuban Type<span className="text-red-600 px-2">*</span>
                      </label>
                      <select
                        name="accountType"
                        id="accountType"
                        value={showhide}
                        onChange={handlesshowhide}
                        className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      >
                        <option value="">Select</option>
                        {typedata.map((data) => (
                          <optgroup key={uuidv4()}>
                            <option className="capatlize text-lg">
                              {data.strNubanType}
                            </option>
                          </optgroup>
                        ))}
                      </select>
                      {error && showhide.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Nuban type
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3 py-1 text-base font-normal text-gray-700 bg-white  "
                      >
                        Nuban Type Description
                      </label>
                      <input
                        type="text"
                        value={desc}
                        disabled={!showhide}
                        onChange={(e) => setDesc(e.target.value.toUpperCase())}
                        autoComplete="off"
                        placeholder=" Enter GL Description"
                        className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      />
                      {error && desc.length <= 0 ? (
                        <p className="text-red-500 text-xs font-medium">
                          Please Enter Nuban Type Description
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    {showhide === "OFI" && (
                      <div className="flex">
                        <label
                          htmlFor="text"
                          className="block w-auto px-3 py-1 text-base font-normal text-gray-700 bg-white  "
                          // className="block text-xs font-semibold text-gray-700"
                        >
                          OFI Code Configuration
                        </label>
                        <div className="flex">
                          <input
                            type="text"
                            value={numberCode}
                            disabled
                            onChange={(e) => setNumberCode(e.target.value)}
                            autoComplete="off"
                            className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          />
                          <input
                            type="text"
                            autoComplete="off"
                            maxLength={5}
                            value={code}
                            disabled={!desc}
                            onChange={(e) => setCode(e.target.value)}
                            className="block w-60 px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                            placeholder=""
                          />
                          {error && code.length <= 0 ? (
                            <p className="text-red-500   text-xs font-medium">
                              Please Enter Code Configuration
                            </p>
                          ) : (
                            ""
                          )}
                        </div>
                      </div>
                    )}

                    {showhide === "DMB" && (
                      <div className="flex">
                        <label
                          htmlFor="text"
                          className="block w-auto px-3 py-1 text-base font-normal text-gray-700 bg-white  "
                          // className="block text-xs font-semibold text-gray-700"
                        >
                          DMB Code Configuration
                        </label>
                        <div className="flex">
                          <input
                            type="text"
                            defaultValue={456}
                            onChange={(e) => setCode(e.target.value)}
                            autoComplete="off"
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                          />

                          <input
                            type="text"
                            autoComplete="off"
                            maxLength={3}
                            minLength={3}
                            value={code}
                            disabled={!desc}
                            // onChange={(e) => setCode(e.target.value)}
                            onChange={(e) => {
                              const value = e.target.value;
                              setCode(value);

                              const regex = /^[0-9]*$/;
                              if (!regex.test(value)) {
                                setErrorMessage(
                                  " Only allow numeric digits"
                                  // "Please enter only numeric values"
                                );
                              } else {
                                setErrorMessage("");
                              }
                              setError("");
                            }}
                            onBlur={() => {
                              if (code.length === 0) {
                                setError("Please enter DMB Code!");
                                setErrorMessage("");
                              }
                            }}
                            className=" p-2 block w-60 shadow-sm sm:text-xs border-gray-300 border rounded-r-md"
                            placeholder=""
                          />
                          {errorMessage ? (
                            <p className="text-red-500 text-xs font-medium">
                              {errorMessage}
                            </p>
                          ) : error && code.length <= 0 ? (
                            <p className="text-red-500 text-xs font-medium">
                              Please enter DMB Code!
                            </p>
                          ) : null}
                        </div>
                      </div>
                    )}
                  </div>
                </div>

                <div className="bg-gray-100 px-4 py-2 text-right sm:px-2 ">
                  <button
                    title="Click Submit Button"
                    type="submit"
                    data-modal-toggle="defaultModal"
                    className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-5  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                  >
                    Submit
                  </button>
                  <button
                    title="Search Data"
                    type="button"
                    onClick={handleClick}
                    className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-5 mx-4 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                  >
                    Clear
                  </button>
                </div>
              </div>
            </form>
          </div>
        </div>
      )}
      {/* </div> */}

      {/* view Form  */}
      {createview === true && (
        <div className="max-w-7xl mx-auto h-full">
          <div className="w-full shadow-md mt-2 bg-blue-200">
            <div className="px-4 sm:px-10 rounded-t-lg ">
              <div className=" flex  items-center justify-between">
                <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                  Nuban Type Configuration
                </p>
              </div>
            </div>
          </div>
          <div className=" md:col-span-2 lg:col-span-1 sm:rounded-md bg-white">
            <form action="#" method="POST">
              <div className="overflow-hidden shadow sm:rounded-md">
                <div className="px-4  sm:p-2 bg-white">
                  <div className="grid gap-2  md:grid-cols-1 px-8 ">
                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3 mr-16 ml-3 py-1 text-base font-normal text-gray-700 bg-white  "
                        // className="block text-xs font-semibold text-gray-700"
                      >
                        Nuban Types
                      </label>
                      <input
                        type="text"
                        autoComplete="off"
                        defaultValue={ntype}
                        disabled
                        placeholder="Enter GLAccount Type"
                        className="block w-60 px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      />
                    </div>
                    <div className="flex">
                      <label
                        htmlFor="text"
                        className="block w-auto px-3 py-1 text-base font-normal text-gray-700 bg-white  "
                      >
                        Nuban Type Description
                      </label>
                      <input
                        type="text"
                        defaultValue={nDescription}
                        disabled
                        autoComplete="off"
                        placeholder="Nuban Type Description"
                        className="block w-60 px-3 py-1 text-base font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                      />
                    </div>

                    <div>
                      {ntype === "OFI" && (
                        <div className="flex">
                          <label
                            htmlFor="text"
                            className="block w-auto px-3 py-1 mr-1 text-base font-normal text-gray-700 bg-white  "
                            // className="block text-xs font-semibold text-gray-700"
                          >
                            OFI Code Configuration
                          </label>
                          <div className="flex">
                            <input
                              type="text"
                              value={9}
                              disabled
                              autoComplete="off"
                              className="  p-2 block w-12  shadow-sm sm:text-sm border-gray-300 border rounded-l-md"
                            />
                            <input
                              type="text"
                              autoComplete="off"
                              defaultValue={code1}
                              disabled
                              className="block w-48 px-3 py-1 text-base font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                              placeholder="Enter Nuban code"
                            />
                          </div>
                        </div>
                      )}

                      {ntype === "DMB" && (
                        <div className="flex">
                          <label
                            htmlFor="text"
                            className="block w-auto px-3 py-1 text-base font-normal text-gray-700 bg-white  "
                            // className="block text-xs font-semibold text-gray-700"
                          >
                            DMB Code Configuration
                          </label>
                          <div className="flex">
                            <input
                              type="text"
                              defaultValue={456}
                              disabled
                              autoComplete="off"
                              className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                            />

                            <input
                              type="text"
                              autoComplete="off"
                              defaultValue={code1}
                              placeholder="Enter nuban code"
                              disabled
                              className=" bg-gray-100 p-2 block w-60 shadow-sm sm:text-xs border-gray-300 border rounded-r-md"
                            />
                          </div>
                        </div>
                      )}
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        </div>
      )}
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* </div> */}
    </AppLayout>
  );
};

export default NubanTypeConfig;
