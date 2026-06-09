import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { NavLink } from "react-router-dom";
import CustomAlert from "../../layout/CustomAlert";
export default function ApproveAccount() {
  const [alldata, setAlldata] = useState([]);
  const [checkpoint, setCheckpoint] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");

  // Function to handle changes in the search input field
  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };
  // Filter the acceptData array based on the searchKeyword
  const filteredData = alldata.filter((data) => {
    const { strFirstName, strLastName, strAccountType, strMobileNo } = data;
    const lowerCasedSearchKeyword = searchKeyword.toLowerCase();
    return (
      (strFirstName &&
        strFirstName.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strLastName &&
        strLastName.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strAccountType &&
        strAccountType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strMobileNo &&
        strMobileNo.toLowerCase().includes(lowerCasedSearchKeyword))
    );
  });
  const highlightKeyword = (text, keyword) => {
    if (!keyword || typeof text !== "string") {
      return text; // No keyword to highlight or invalid text
    }

    const regex = new RegExp(`(${escapeRegExp(keyword)})`, "gi");
    const highlightedText = text.replace(
      regex,
      '<span className="bg-yellow-200">$1</span>'
    );
    return <div dangerouslySetInnerHTML={{ __html: highlightedText }} />;
  };
  const escapeRegExp = (string) => {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"); // Escape special characters
  };
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

  // Table List
  useEffect(() => {
    tabledata();
  }, []);

  const tabledata = async () => {
    try {
      const response = await amsApi.post(
        `preAccountMaster/nonLinkedCustmerForAccountNo`,
        {}
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.preAccountMasters);
        setCheckpoint(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Customer List To Link Account Number
              </p>
            </div>
          </div>
        </div>

        <div className="flex justify-end items-center sm:px-6 lg:px-8 bg-blue-100 rounded-t-lg mt-1 ">
          <div className=" flex justify-center  rounded-md border border-transparent  px-5 mx-4 text-xs  font-medium text-white ">
            <div className="pt-2 relative  mx-auto text-gray-600">
              <input
                type="text"
                title="Search Data"
                value={searchKeyword}
                onChange={handleSearch}
                className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                placeholder="Search.."
                style={{
                  backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                  // You can set other styles here as needed
                }}
                disabled={checkpoint !== 1}
              />
              {""}
              <button
                type="submit"
                className="absolute right-0 top-0 mt-5 mr-4 px-4  "
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                  strokeWidth={1.5}
                  stroke="currentColor"
                  className="w-6 h-6"
                >
                  <path
                    strokeLinecap="round"
                    strokeLinejoin="round"
                    d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                  />
                </svg>
              </button>
            </div>
          </div>
        </div>

        {/* table */}
        <div className="bg-white overflow-x-auto relative shadow-md ">
          <div className="overflow-x-auto relative shadow-md  ">
            <table className="w-full text-xs text-left">
              <thead className="text-xm border-b sticky top-0 text-gray-500 uppercase bg-gray-100 dark:text-white">
                <tr>
                  <th scope="col" className="py-2.5 px-6">
                    FIRST NAME
                  </th>
                  <th scope="col" className="py-2.5 px-6">
                    LAST NAME
                  </th>
                  <th scope="col" className="py-2.5 px-6">
                    ACCOUNT TYPE
                  </th>
                  <th scope="col" className="py-2.5 px-6">
                    MOBILE NUMBER
                  </th>
                  <th scope="col" className="py-2.5 px-6">
                    LINK ACCOUNT
                  </th>
                </tr>
              </thead>
              <tbody>
                {filteredData.length > 0 ? (
                  <>
                    {filteredData.map(
                      ({
                        strFirstName,
                        strLastName,
                        strAccountType,
                        strMobileNo,
                      }) => (
                        <tr className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400">
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <span className="text-xs font-medium text-gray-900">
                              {highlightKeyword(
                                strFirstName || "-",
                                searchKeyword
                              )}
                            </span>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <span className="text-xs font-medium text-gray-900">
                              {highlightKeyword(
                                strLastName || "-",
                                searchKeyword
                              )}
                            </span>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <span className="text-xs font-medium text-gray-900">
                              {highlightKeyword(
                                strAccountType || "-",
                                searchKeyword
                              )}
                            </span>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <span className="text-xs font-medium text-gray-900">
                              {highlightKeyword(
                                strMobileNo || "-",
                                searchKeyword
                              )}
                            </span>
                          </td>
                          <td className="px-6 py-2.5 whitespace-nowrap">
                            <span className="text-xs font-medium text-blue-600">
                              <NavLink
                                title="Click Here to link"
                                to={`/approve_link_account/${strMobileNo}`}
                                className="border-blue-300 shadow-md p-2 bg-blue-300 text-black  rounded-full focus:ring-blue-400"
                              >
                                {/* {strTxnId || "-"}{ ""                    } */}
                                <span text={"red"}>Click here to link</span>
                              </NavLink>
                            </span>
                          </td>
                        </tr>
                      )
                    )}
                  </>
                ) : (
                  <td colSpan="12" className="px-6 py-1.5 text-center">
                    <span className="text-sm font-normal text-gray-500">
                      No data available.
                    </span>
                  </td>
                )}
              </tbody>
            </table>
          </div>
        </div>
        {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
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
